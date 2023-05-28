package io.daobab.target.database;

import io.daobab.error.DaobabException;
import io.daobab.target.Target;
import io.daobab.target.database.transaction.OpenTransactionDataBaseTargetImpl;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

public interface TransactionalTarget extends Target, QueryDataBaseHandler {

//    TransactionalTarget getSourceTarget();

    //TODO: wywal database na rzecz czysciocha
    default OpenTransactionDataBaseTargetImpl beginTransaction() {
        return new OpenTransactionDataBaseTargetImpl((DataBaseTarget) this);
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
            otx.rollback();
            throw new DaobabException("Transaction related exception", e);
        }

    }
}
