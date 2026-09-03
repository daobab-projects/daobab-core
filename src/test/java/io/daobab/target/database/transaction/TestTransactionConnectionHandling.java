package io.daobab.target.database.transaction;

import io.daobab.error.DaobabException;
import io.daobab.error.DaobabSQLException;
import io.daobab.error.TransactionClosedException;
import io.daobab.error.TransactionNotAllowerForPropagationNever;
import io.daobab.error.TransactionOpenedException;
import io.daobab.target.database.MockDataBase;
import io.daobab.test.dao.table.Actor;
import io.daobab.transaction.Propagation;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * How a database target hands out, commits and closes its connections.
 * <p>
 * The statements running inside an {@link OpenTransactionDataBaseTargetImpl} used to be delegated to the source
 * target, which took a connection of its own for each of them: they ran (and committed) outside the transaction,
 * so {@code rollback()} could not undo them and {@code commit()} had nothing to commit. A query such as
 * {@code readPlateList} went further and closed the transaction's connection underneath it. These tests pin the
 * connection bookkeeping down: inside a transaction exactly one connection is used, and only the transaction
 * commits and closes it; outside a transaction every statement still finishes its own connection.
 * <p>
 * The JDBC layer is a recording stub, so no database is involved.
 */
class TestTransactionConnectionHandling {

    private static final Actor TAB = new Actor();

    // ---------------------------------------------------------------- inside a transaction

    @Test
    void everyStatementOfATransactionSharesItsOneConnection() {
        StubTarget target = new StubTarget();
        OpenTransactionDataBaseTargetImpl tx = target.beginTransaction();

        tx.delete(TAB).whereEqual(TAB.colActorId(), 1).execute(false);
        tx.delete(TAB).whereEqual(TAB.colActorId(), 2).execute(false);

        assertEquals(1, target.log.connectionsOpened, "the transaction's connection is the only one used");
        assertEquals(2, target.log.statements.size(), "both statements went to the database");
        assertEquals(0, target.log.commits, "nothing may be committed before the transaction says so");
        assertEquals(0, target.log.closes, "the transaction's connection must stay open");
    }

    @Test
    void onlyTheTransactionCommitsAndClosesItsConnection() {
        StubTarget target = new StubTarget();
        OpenTransactionDataBaseTargetImpl tx = target.beginTransaction();

        tx.delete(TAB).whereEqual(TAB.colActorId(), 1).execute(false);
        tx.commit();

        assertEquals(1, target.log.commits);
        assertEquals(1, target.log.closes);
        assertEquals(List.of(false), target.log.autoCommits, "autoCommit is turned off once, by the transaction");
    }

    @Test
    void aRollbackReachesEveryStatementOfTheTransaction() {
        StubTarget target = new StubTarget();
        OpenTransactionDataBaseTargetImpl tx = target.beginTransaction();

        tx.delete(TAB).whereEqual(TAB.colActorId(), 1).execute(false);
        tx.insert(actor(7, "PENELOPE", "GUINESS")).execute(false);
        tx.rollback();

        assertEquals(1, target.log.connectionsOpened);
        assertEquals(0, target.log.commits, "a rolled back transaction must not have committed anything");
        assertEquals(1, target.log.rollbacks);
        assertEquals(1, target.log.closes);
    }

    @Test
    void aReadInsideATransactionDoesNotCloseItsConnection() {
        StubTarget target = new StubTarget();
        OpenTransactionDataBaseTargetImpl tx = target.beginTransaction();

        //readPlateList closed the connection unconditionally, unlike doSthOnConnection
        tx.select(TAB.colActorId(), TAB.colFirstName()).findMany();
        tx.select(TAB.colActorId()).findOne();
        tx.select(TAB.colFirstName()).findMany();

        assertEquals(1, target.log.connectionsOpened);
        assertEquals(0, target.log.closes, "a read must leave the transaction's connection alone");

        //...and the transaction is still usable afterwards
        tx.delete(TAB).whereEqual(TAB.colActorId(), 1).execute(false);
        tx.commit();
        assertEquals(1, target.log.commits);
        assertEquals(1, target.log.closes);
    }

    @Test
    void insertAllCommitsEveryEntityAtOnce() {
        StubTarget target = new StubTarget();

        target.insertAll(List.of(
                actor(1, "NICK", "WAHLBERG"),
                actor(2, "ED", "CHASE"),
                actor(3, "JENNIFER", "DAVIS")));

        assertEquals(1, target.log.connectionsOpened, "insertAll wraps every insert in one transaction");
        assertEquals(3, target.log.statements.size());
        assertEquals(1, target.log.commits, "one commit for the whole batch, not one per row");
        assertEquals(1, target.log.closes);
    }

    @Test
    void aClosedTransactionRefusesFurtherWork() {
        StubTarget target = new StubTarget();
        OpenTransactionDataBaseTargetImpl tx = target.beginTransaction();
        tx.commit();

        assertThrows(TransactionClosedException.class, () -> tx.delete(TAB).whereEqual(TAB.colActorId(), 1).execute(false));
        assertEquals(1, target.log.connectionsOpened, "a finished transaction does not silently open a new connection");
    }

    @Test
    void aRollbackAfterTheTransactionFinishedIsANoOp() {
        StubTarget target = new StubTarget();
        OpenTransactionDataBaseTargetImpl tx = target.beginTransaction();
        tx.commit();

        tx.rollback();

        assertEquals(1, target.log.commits);
        assertEquals(0, target.log.rollbacks, "there is nothing left to roll back");
        assertEquals(1, target.log.closes);
    }

    // ---------------------------------------------------------------- outside a transaction

    @Test
    void aStatementOutsideATransactionFinishesItsOwnConnection() {
        StubTarget target = new StubTarget();

        target.delete(TAB).whereEqual(TAB.colActorId(), 1).execute(true);

        assertEquals(1, target.log.connectionsOpened);
        assertEquals(1, target.log.commits);
        assertEquals(1, target.log.closes);
        assertEquals(List.of(false), target.log.autoCommits);
    }

    @Test
    void aNonTransactionalStatementNeitherCommitsNorTouchesAutoCommit() {
        StubTarget target = new StubTarget();

        target.delete(TAB).whereEqual(TAB.colActorId(), 1).execute(false);

        assertEquals(1, target.log.connectionsOpened);
        assertEquals(0, target.log.commits);
        assertEquals(List.of(), target.log.autoCommits);
        assertEquals(1, target.log.closes, "its own connection still has to be returned");
    }

    @Test
    void aFailedStatementRollsBackInsteadOfCommitting() {
        StubTarget target = new StubTarget();
        target.failOnExecute = true;

        assertThrows(DaobabSQLException.class,
                () -> target.delete(TAB).whereEqual(TAB.colActorId(), 1).execute(true));

        assertEquals(0, target.log.commits, "a statement that threw must never be committed");
        assertEquals(1, target.log.rollbacks);
        assertEquals(1, target.log.closes);
    }

    // ---------------------------------------------------------------- propagation

    @Test
    void propagationRequiredRunsTheWorkOnTheTransactionItStarted() {
        StubTarget target = new StubTarget();

        target.delete(TAB).whereEqual(TAB.colActorId(), 1).execute(Propagation.REQUIRED);

        //the propagation used to open a transaction and then hand the statement to the source target, which took
        //a second connection and committed it itself, leaving the transaction with nothing to commit
        assertEquals(1, target.log.connectionsOpened);
        assertEquals(1, target.log.commits);
        assertEquals(1, target.log.closes);
    }

    @Test
    void propagationMandatoryInsideATransactionStaysOnIt() {
        StubTarget target = new StubTarget();
        OpenTransactionDataBaseTargetImpl tx = target.beginTransaction();

        tx.delete(TAB).whereEqual(TAB.colActorId(), 1).execute(Propagation.MANDATORY);

        assertEquals(1, target.log.connectionsOpened);
        assertEquals(0, target.log.commits, "the enclosing transaction decides when to commit");

        tx.commit();
        assertEquals(1, target.log.commits);
    }

    @Test
    void propagationRequiredRollsTheStartedTransactionBackOnFailure() {
        StubTarget target = new StubTarget();
        target.failOnExecute = true;

        DaobabException thrown = assertThrows(DaobabException.class,
                () -> target.delete(TAB).whereEqual(TAB.colActorId(), 1).execute(Propagation.REQUIRED));
        assertTrue(thrown.getMessage().contains("Transaction related exception"), thrown.getMessage());

        assertEquals(0, target.log.commits);
        assertEquals(1, target.log.rollbacks);
        assertEquals(1, target.log.closes);
    }

    // ---------------------------------------------------------------- propagations escaping a transaction

    @Test
    void propagationRequiredNewInsideATransactionRunsInATransactionOfItsOwn() {
        StubTarget target = new StubTarget();
        OpenTransactionDataBaseTargetImpl tx = target.beginTransaction();

        tx.delete(TAB).whereEqual(TAB.colActorId(), 1).execute(false);
        tx.delete(TAB).whereEqual(TAB.colActorId(), 2).execute(Propagation.REQUIRED_NEW);

        assertEquals(2, target.log.connectionsOpened, "the new transaction gets a connection of its own");
        assertEquals(1, target.log.commits, "only the new transaction committed");
        assertEquals(1, target.log.closes, "only the new transaction's connection was returned");
        assertEquals(List.of(false, false), target.log.autoCommits, "both transactions turned autoCommit off");

        //the suspended transaction kept its connection and is still usable
        tx.delete(TAB).whereEqual(TAB.colActorId(), 3).execute(false);
        tx.commit();

        assertEquals(2, target.log.commits);
        assertEquals(2, target.log.closes);
        assertEquals(3, target.log.statements.size());
    }

    @Test
    void propagationRequiredNewRollsBackOnlyItsOwnTransaction() {
        StubTarget target = new StubTarget();
        OpenTransactionDataBaseTargetImpl tx = target.beginTransaction();

        tx.delete(TAB).whereEqual(TAB.colActorId(), 1).execute(false);

        target.failOnExecute = true;
        assertThrows(DaobabException.class,
                () -> tx.delete(TAB).whereEqual(TAB.colActorId(), 2).execute(Propagation.REQUIRED_NEW));
        target.failOnExecute = false;

        assertEquals(2, target.log.connectionsOpened);
        assertEquals(0, target.log.commits);
        assertEquals(1, target.log.rollbacks, "the failure rolled back the new transaction only");
        assertEquals(1, target.log.closes);

        //the outer transaction survived the failure of the independent one and still commits its own work
        tx.delete(TAB).whereEqual(TAB.colActorId(), 3).execute(false);
        tx.commit();

        assertEquals(1, target.log.commits);
        assertEquals(1, target.log.rollbacks);
        assertEquals(2, target.log.closes);
    }

    @Test
    void propagationNotSupportedInsideATransactionRunsOutsideIt() {
        StubTarget target = new StubTarget();
        OpenTransactionDataBaseTargetImpl tx = target.beginTransaction();

        tx.delete(TAB).whereEqual(TAB.colActorId(), 1).execute(Propagation.NOT_SUPPORTED);

        assertEquals(2, target.log.connectionsOpened, "the suspended work runs on a connection of its own");
        assertEquals(0, target.log.commits, "it runs non-transactionally, so there is nothing to commit");
        assertEquals(1, target.log.closes, "its own connection is returned right away");
        assertEquals(List.of(false), target.log.autoCommits, "only the suspended transaction turned autoCommit off");

        //the suspended transaction kept its connection and is still usable
        tx.delete(TAB).whereEqual(TAB.colActorId(), 2).execute(false);
        tx.commit();

        assertEquals(1, target.log.commits);
        assertEquals(2, target.log.closes);
    }

    @Test
    void propagationNotSupportedOutsideATransactionChangesNothing() {
        StubTarget target = new StubTarget();

        target.delete(TAB).whereEqual(TAB.colActorId(), 1).execute(Propagation.NOT_SUPPORTED);

        assertEquals(1, target.log.connectionsOpened);
        assertEquals(0, target.log.commits);
        assertEquals(1, target.log.closes);
        assertEquals(List.of(), target.log.autoCommits);
    }

    @Test
    void propagationSupportsInsideATransactionJoinsItInsteadOfEscaping() {
        StubTarget target = new StubTarget();
        OpenTransactionDataBaseTargetImpl tx = target.beginTransaction();

        tx.delete(TAB).whereEqual(TAB.colActorId(), 1).execute(Propagation.SUPPORTS);

        assertEquals(1, target.log.connectionsOpened, "SUPPORTS joins the open transaction");
        assertEquals(0, target.log.commits);

        tx.commit();
        assertEquals(1, target.log.commits);
    }

    @Test
    void propagationNeverInsideATransactionIsRejected() {
        StubTarget target = new StubTarget();
        OpenTransactionDataBaseTargetImpl tx = target.beginTransaction();

        assertThrows(TransactionNotAllowerForPropagationNever.class,
                () -> tx.delete(TAB).whereEqual(TAB.colActorId(), 1).execute(Propagation.NEVER));

        assertEquals(1, target.log.connectionsOpened, "the rejected statement did not reach for a connection");
        assertEquals(0, target.log.statements.size());
    }

    @Test
    void propagationMandatoryOutsideATransactionIsRejected() {
        StubTarget target = new StubTarget();

        assertThrows(TransactionClosedException.class,
                () -> target.delete(TAB).whereEqual(TAB.colActorId(), 1).execute(Propagation.MANDATORY));

        assertEquals(0, target.log.connectionsOpened);
    }

    // ---------------------------------------------------------------- NESTED, on savepoints

    @Test
    void propagationNestedInsideATransactionRollsBackOnlyItsOwnWork() {
        StubTarget target = new StubTarget();
        OpenTransactionDataBaseTargetImpl tx = target.beginTransaction();

        //work of the enclosing transaction, which the nested failure must not touch
        tx.delete(TAB).whereEqual(TAB.colActorId(), 1).execute(false);

        target.failOnExecute = true;
        assertThrows(DaobabSQLException.class,
                () -> tx.delete(TAB).whereEqual(TAB.colActorId(), 2).execute(Propagation.NESTED));
        target.failOnExecute = false;

        assertEquals(1, target.log.connectionsOpened, "a nested transaction stays on the enclosing connection");
        assertEquals(1, target.log.savepointsSet);
        assertEquals(1, target.log.savepointRollbacks, "the nested work was rolled back to its savepoint");
        assertEquals(0, target.log.rollbacks, "the enclosing transaction was not rolled back");
        assertEquals(0, target.log.closes, "nor was its connection closed");

        //the enclosing transaction is still open and commits the work it did before the nested failure
        tx.delete(TAB).whereEqual(TAB.colActorId(), 3).execute(false);
        tx.commit();

        assertEquals(1, target.log.commits);
        assertEquals(1, target.log.closes);
    }

    @Test
    void propagationNestedReleasesItsSavepointOnSuccess() {
        StubTarget target = new StubTarget();
        OpenTransactionDataBaseTargetImpl tx = target.beginTransaction();

        tx.delete(TAB).whereEqual(TAB.colActorId(), 1).execute(Propagation.NESTED);

        assertEquals(1, target.log.connectionsOpened);
        assertEquals(1, target.log.savepointsSet);
        assertEquals(1, target.log.savepointReleases);
        assertEquals(0, target.log.savepointRollbacks);
        assertEquals(0, target.log.commits, "the enclosing transaction still decides when to commit");

        tx.commit();
        assertEquals(1, target.log.commits);
    }

    @Test
    void propagationNestedWithNothingToNestIntoStartsATransaction() {
        StubTarget target = new StubTarget();

        target.delete(TAB).whereEqual(TAB.colActorId(), 1).execute(Propagation.NESTED);

        assertEquals(0, target.log.savepointsSet, "there is no transaction to take a savepoint on");
        assertEquals(1, target.log.connectionsOpened);
        assertEquals(1, target.log.commits, "so NESTED behaves like REQUIRED");
        assertEquals(1, target.log.closes);
    }

    @Test
    void everyNestedTransactionOfATransactionGetsItsOwnSavepoint() {
        StubTarget target = new StubTarget();
        OpenTransactionDataBaseTargetImpl tx = target.beginTransaction();

        tx.delete(TAB).whereEqual(TAB.colActorId(), 1).execute(Propagation.NESTED);

        target.failOnExecute = true;
        assertThrows(DaobabSQLException.class,
                () -> tx.delete(TAB).whereEqual(TAB.colActorId(), 2).execute(Propagation.NESTED));
        target.failOnExecute = false;

        tx.delete(TAB).whereEqual(TAB.colActorId(), 3).execute(Propagation.NESTED);
        tx.commit();

        assertEquals(3, target.log.savepointsSet);
        assertEquals(2, target.log.savepointReleases, "the two that succeeded released their savepoint");
        assertEquals(1, target.log.savepointRollbacks, "the one that failed rolled back to its own");
        assertEquals(0, target.log.rollbacks);
        assertEquals(1, target.log.commits, "the enclosing transaction committed the two that survived");
        assertEquals(1, target.log.connectionsOpened);
    }

    @Test
    void aNestedTransactionSurvivesADriverThatCannotReleaseSavepoints() {
        StubTarget target = new StubTarget();
        target.failOnReleaseSavepoint = true;
        OpenTransactionDataBaseTargetImpl tx = target.beginTransaction();

        //must not fail: the savepoint disappears with the enclosing transaction anyway
        tx.delete(TAB).whereEqual(TAB.colActorId(), 1).execute(Propagation.NESTED);

        assertEquals(1, target.log.savepointsSet);
        assertEquals(0, target.log.savepointReleases);
        assertEquals(0, target.log.savepointRollbacks);

        tx.commit();
        assertEquals(1, target.log.commits);
    }

    @Test
    void propagationNestedOnAFinishedTransactionIsRejected() {
        StubTarget target = new StubTarget();
        OpenTransactionDataBaseTargetImpl tx = target.beginTransaction();
        tx.commit();

        assertThrows(TransactionClosedException.class,
                () -> tx.delete(TAB).whereEqual(TAB.colActorId(), 1).execute(Propagation.NESTED));

        assertEquals(0, target.log.savepointsSet, "no savepoint on a connection the transaction already returned");
        assertEquals(1, target.log.connectionsOpened, "and no fresh connection taken behind the caller's back");
    }

    // ---------------------------------------------------------------- a finished transaction handle

    @Test
    void everyPropagationOnAFinishedTransactionIsRejected() {
        //a finished handle reports no active transaction, which used to be indistinguishable from a plain target:
        //the propagation fell back to starting a new transaction and ran the work on a connection of its own
        for (Propagation propagation : Propagation.values()) {
            StubTarget target = new StubTarget();
            OpenTransactionDataBaseTargetImpl tx = target.beginTransaction();
            tx.commit();

            assertThrows(TransactionClosedException.class,
                    () -> tx.delete(TAB).whereEqual(TAB.colActorId(), 1).execute(propagation),
                    "propagation " + propagation);

            assertEquals(1, target.log.connectionsOpened, "no connection taken for " + propagation);
            assertEquals(0, target.log.statements.size(), "no statement sent for " + propagation);
            assertEquals(0, target.log.savepointsSet, "no savepoint taken for " + propagation);
        }
    }

    @Test
    void aFinishedTransactionRefusesReadsAsWellAsWrites() {
        StubTarget target = new StubTarget();
        OpenTransactionDataBaseTargetImpl tx = target.beginTransaction();
        tx.commit();

        assertThrows(TransactionClosedException.class, () -> tx.select(TAB.colActorId()).findMany());
        assertThrows(TransactionClosedException.class, () -> tx.select(TAB.colActorId()).findOne());
        assertThrows(TransactionClosedException.class, () -> tx.select(TAB).findMany());

        assertEquals(1, target.log.connectionsOpened);
    }

    @Test
    void aFinishedTransactionReportsBeingClosedRatherThanOpen() {
        StubTarget target = new StubTarget();
        OpenTransactionDataBaseTargetImpl tx = target.beginTransaction();
        tx.commit();

        assertThrows(TransactionClosedException.class, tx::commit);
        //beginTransaction used to answer "already opened" for a transaction that had in fact been closed
        assertThrows(TransactionClosedException.class, tx::beginTransaction);
    }

    @Test
    void anOpenTransactionStillRefusesToBeginAnotherOne() {
        StubTarget target = new StubTarget();
        OpenTransactionDataBaseTargetImpl tx = target.beginTransaction();

        assertThrows(TransactionOpenedException.class, tx::beginTransaction);

        tx.commit();
        assertEquals(1, target.log.commits);
    }

    // ---------------------------------------------------------------- the recording JDBC stub

    private static Actor actor(int id, String first, String last) {
        return new Actor().setActorId(id).setFirstName(first).setLastName(last);
    }

    /** What the target did to the connections it was handed. */
    private static final class JdbcLog {
        final List<Boolean> autoCommits = new ArrayList<>();
        final List<String> statements = new ArrayList<>();
        int connectionsOpened;
        int commits;
        int rollbacks;
        int closes;
        int savepointsSet;
        int savepointRollbacks;
        int savepointReleases;
    }

    /**
     * A target whose {@code DataSource} hands out recording connections. {@code getDataSource()} is overridden
     * rather than {@code initDataSource()}, so the heavy metadata bootstrap of a real target is skipped.
     */
    private static final class StubTarget extends MockDataBase {

        final JdbcLog log = new JdbcLog();
        boolean failOnExecute;
        boolean failOnReleaseSavepoint;

        private final DataSource dataSource = proxy(DataSource.class, (method, args) -> {
            if ("getConnection".equals(method.getName())) {
                log.connectionsOpened++;
                return stubConnection();
            }
            return null;
        });

        @Override
        public DataSource getDataSource() {
            return dataSource;
        }

        @Override
        public OpenTransactionDataBaseTargetImpl beginTransaction() {
            return new OpenTransactionDataBaseTargetImpl(this);
        }

        private Connection stubConnection() {
            boolean[] closed = {false};
            return proxy(Connection.class, (method, args) -> switch (method.getName()) {
                case "commit" -> {
                    log.commits++;
                    yield null;
                }
                case "rollback" -> {
                    //rollback() and rollback(Savepoint) share a name: counting them together would let a nested
                    //rollback pass for a rollback of the whole transaction
                    if (args == null || args.length == 0) {
                        log.rollbacks++;
                    } else {
                        log.savepointRollbacks++;
                    }
                    yield null;
                }
                case "setSavepoint" -> {
                    requireOpen(closed[0]);
                    log.savepointsSet++;
                    yield stubSavepoint(log.savepointsSet);
                }
                case "releaseSavepoint" -> {
                    if (failOnReleaseSavepoint) {
                        //Oracle and Microsoft SQL Server cannot release a savepoint
                        throw new SQLException("releasing a savepoint is not supported");
                    }
                    log.savepointReleases++;
                    yield null;
                }
                case "close" -> {
                    log.closes++;
                    closed[0] = true;
                    yield null;
                }
                case "isClosed" -> closed[0];
                case "setAutoCommit" -> {
                    requireOpen(closed[0]);
                    log.autoCommits.add((Boolean) args[0]);
                    yield null;
                }
                case "getAutoCommit" -> false;
                case "prepareStatement" -> {
                    //a real driver refuses to work on a returned connection, and that is exactly what a
                    //statement leaking out of its transaction ends up doing
                    requireOpen(closed[0]);
                    log.statements.add((String) args[0]);
                    yield stubStatement();
                }
                default -> null;
            });
        }

        private PreparedStatement stubStatement() {
            boolean[] closed = {false};
            return proxy(PreparedStatement.class, (method, args) -> switch (method.getName()) {
                case "executeUpdate" -> {
                    if (failOnExecute) {
                        throw new SQLException("stub failure");
                    }
                    yield 1;
                }
                case "executeQuery", "getGeneratedKeys" -> {
                    if (failOnExecute) {
                        throw new SQLException("stub failure");
                    }
                    yield emptyResultSet();
                }
                case "close" -> {
                    closed[0] = true;
                    yield null;
                }
                case "isClosed" -> closed[0];
                default -> null;
            });
        }

        private static Savepoint stubSavepoint(int id) {
            return proxy(Savepoint.class, (method, args) -> switch (method.getName()) {
                case "getSavepointId" -> id;
                case "getSavepointName" -> "savepoint" + id;
                default -> null;
            });
        }

        private static void requireOpen(boolean closed) throws SQLException {
            if (closed) {
                throw new SQLException("the connection is already closed");
            }
        }

        private static ResultSet emptyResultSet() {
            return proxy(ResultSet.class, (method, args) -> "next".equals(method.getName()) ? false : null);
        }
    }

    /** The bit of a JDBC interface a test cares about; everything else falls back to a type-appropriate zero. */
    private interface StubCall {
        Object invoke(java.lang.reflect.Method method, Object[] args) throws Throwable;
    }

    /**
     * A proxy of the given JDBC interface: {@code call} answers the methods that matter, {@link Object}'s own
     * methods are answered by identity, and any other method returns the default value of its return type (so an
     * unexpected {@code int} setter does not blow up on an unboxing of {@code null}).
     */
    @SuppressWarnings("unchecked")
    private static <T> T proxy(Class<T> type, StubCall call) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, (instance, method, args) -> {
            switch (method.getName()) {
                case "toString":
                    return "stub" + type.getSimpleName();
                case "hashCode":
                    return System.identityHashCode(instance);
                case "equals":
                    return instance == args[0];
                default:
                    break;
            }
            Object rv;
            try {
                rv = call.invoke(method, args);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
            return rv == null ? defaultValue(method.getReturnType()) : rv;
        });
    }

    private static Object defaultValue(Class<?> returnType) {
        if (!returnType.isPrimitive() || void.class.equals(returnType)) {
            return null;
        }
        if (boolean.class.equals(returnType)) {
            return false;
        }
        if (long.class.equals(returnType)) {
            return 0L;
        }
        if (double.class.equals(returnType)) {
            return 0D;
        }
        if (float.class.equals(returnType)) {
            return 0F;
        }
        if (short.class.equals(returnType)) {
            return (short) 0;
        }
        if (byte.class.equals(returnType)) {
            return (byte) 0;
        }
        if (char.class.equals(returnType)) {
            return (char) 0;
        }
        return 0;
    }
}
