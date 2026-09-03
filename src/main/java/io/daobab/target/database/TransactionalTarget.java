package io.daobab.target.database;

import io.daobab.error.DaobabException;
import io.daobab.target.QueryHandler;
import io.daobab.target.Target;
import io.daobab.target.database.transaction.OpenTransactionDataBaseTargetImpl;
import io.daobab.transaction.Propagation;
import io.daobab.transaction.TransactionIndicator;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public interface TransactionalTarget extends Target, QueryDataBaseHandler {

    /**
     * The target that hands out connections of its own, unaffected by any transaction running on this one.
     * <p>
     * A plain target is its own source. A target representing an open transaction returns the target underneath
     * it, whose {@code getConnection()} takes a fresh connection from the data source - so a propagation that has
     * to leave the current transaction ({@link Propagation#REQUIRED_NEW}, {@link Propagation#NOT_SUPPORTED}) can
     * run its work on a connection that transaction neither owns nor commits. That is what suspending the current
     * transaction amounts to here: the outer transaction keeps its own connection, untouched, and is committed or
     * rolled back later exactly as if the suspended work had never run.
     *
     * @return the target underneath any open transaction, or this target when none is open
     */
    default TransactionalTarget getSourceTarget() {
        return this;
    }

    /**
     * Fails when this target can no longer be given work.
     * <p>
     * A plain target always can. A target representing a transaction refuses once that transaction has been
     * committed or rolled back: without this check a propagation resolved on a finished handle would find no
     * active transaction, fall back to "start a new one" and silently run the work on a fresh connection of the
     * target underneath - while the same call made without a propagation fails on {@code getConnection()}. Using
     * a finished handle is a programming error and has to be reported the same way through every entry point.
     */
    default void validateUsable() {
        //a plain target has no transaction to outlive
    }

    //TODO: wywal database na rzecz czysciocha
    default OpenTransactionDataBaseTargetImpl beginTransaction() {
        //DataBaseTargetLogic is all the transaction needs (it is the constructor parameter too); casting to the
        //DataBaseTarget class would fail for any other implementation of that interface
        return new OpenTransactionDataBaseTargetImpl((DataBaseTargetLogic) this);
    }

    default <R> void wrapTransactionAsynch(Function<OpenTransactionDataBaseTargetImpl, ? extends R> consumer) {
        CompletableFuture.runAsync(() -> wrapTransaction(consumer));
    }

    default <R> void wrapTransactionAsynch(Function<OpenTransactionDataBaseTargetImpl, ? extends R> consumer, Executor executor) {
        CompletableFuture.runAsync(() -> wrapTransaction(consumer), executor);
    }

    default <R> R wrapTransaction(Function<OpenTransactionDataBaseTargetImpl, ? extends R> consumer) {

        OpenTransactionDataBaseTargetImpl otx = beginTransaction();
        try {
            R rv = consumer.apply(otx);
            otx.commit();
            return rv;
        } catch (Exception e) {
            //a no-op when the transaction is already finished (a commit() that threw closed it), so the original
            //cause is never masked by a rollback of an already closed connection
            otx.rollback();
            throw new DaobabException("Transaction related exception", e);
        }

    }

    /**
     * Runs the work as a nested transaction of the transaction currently open on this target: a savepoint is
     * taken before the work and, when the work fails, only the work is rolled back - the enclosing transaction
     * stays open, keeps everything it did before, and still decides on the final commit. On success the
     * savepoint is released, best effort: not every driver supports releasing one.
     * <p>
     * A target with no transaction of its own has nothing to nest into, so it simply runs the work.
     * {@link Propagation#NESTED} never routes here in that case - it starts a transaction instead.
     *
     * @param work the work to run inside the nested transaction
     * @param <R>  the result type
     * @return whatever the work returned
     */
    default <R> R wrapNestedTransaction(Supplier<R> work) {
        return work.get();
    }

    /**
     * Runs a unit of work against this target under the requested {@link Propagation}, handing the work the
     * target it has to run on and whether that run is transactional.
     * <p>
     * When the propagation asks for a brand new transaction, the work runs on the
     * {@link OpenTransactionDataBaseTargetImpl transaction target} itself, so every statement goes through the
     * transaction's own connection and is committed (or rolled back) once, by
     * {@link #wrapTransaction(Function)}. The propagations that have to leave an already open transaction resolve
     * their target through {@link #getSourceTarget()}, so they never borrow its connection.
     *
     * @param target      the target to resolve the propagation against - this target
     * @param propagation the requested propagation
     * @param jobToDo     the work, taking the target to run on and the transactional flag
     * @param <Y>         the result type
     * @param <T>         the target type
     * @return whatever the work returned
     */
    default <Y, T extends TransactionalTarget> Y handleTransactionalTarget(T target, Propagation propagation, BiFunction<QueryHandler, Boolean, Y> jobToDo) {
        //before the propagation is resolved: a finished transaction reports no active transaction, which would
        //otherwise be indistinguishable from a plain target that never had one
        target.validateUsable();
        TransactionIndicator indicator = propagation.mayBeProceeded(target);
        switch (indicator) {
            case EXECUTE_WITHOUT:
                //NOT_SUPPORTED suspends the transaction running on this target: the work goes to the source
                //target, which gives it a connection of its own, so it is neither committed nor rolled back
                //together with that transaction. With no transaction open the source target is this one.
                return jobToDo.apply(target.getSourceTarget(), false);
            case START_NEW_JUST_FOR_IT:
                //REQUIRED_NEW always runs inside a transaction of its own, started on the source target, so an
                //already open transaction on this target keeps its connection and is left untouched.
                return target.getSourceTarget().wrapTransaction(t -> jobToDo.apply(t, true));
            case GO_AHEAD:
                return jobToDo.apply(target, true);
            case GO_AHEAD_NESTED:
                //NESTED stays on the open transaction's connection, but behind a savepoint, so failing work
                //undoes itself without taking the enclosing transaction down with it
                return target.wrapNestedTransaction(() -> jobToDo.apply(target, true));
            default:
                throw new DaobabException("Problem related to specific propagation and transaction");
        }
    }
}
