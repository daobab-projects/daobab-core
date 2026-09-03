package io.daobab.target.database.transaction;

import io.daobab.error.DaobabSQLException;
import io.daobab.error.TransactionClosedException;
import io.daobab.error.TransactionOpenedException;
import io.daobab.model.Entity;
import io.daobab.model.ProcedureParameters;
import io.daobab.query.base.QuerySpecialParameters;
import io.daobab.statement.base.IdentifierStorage;
import io.daobab.target.BaseTarget;
import io.daobab.target.database.*;
import io.daobab.target.database.connection.ResultSetReader;
import io.daobab.target.database.converter.DatabaseConverterManager;
import io.daobab.target.database.converter.dateformat.DatabaseDateConverter;
import io.daobab.target.database.meta.MetaData;
import io.daobab.target.database.query.*;
import io.daobab.target.database.query.frozen.FrozenQueryProvider;
import io.daobab.target.protection.AccessProtector;
import io.daobab.transaction.Propagation;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class OpenTransactionDataBaseTargetImpl extends BaseTarget implements OpenedTransactionDataBaseTarget, DataBaseTargetLogic, QueryDataBaseHandler {

    private final DataBaseTargetLogic db;
    private final Connection conn;
    private boolean transactionActive;


    //TODO: wywal baze danych zostaw czysty target transakcyjny
    public OpenTransactionDataBaseTargetImpl(DataBaseTargetLogic target) {
        db = target;
        conn = db.getConnection();
        try {
            conn.setAutoCommit(false);
            setTransactionActive(true);
        } catch (SQLException e) {
            throw new DaobabSQLException(e);
        }

    }

    @Override
    public boolean getShowSql() {
        return db.getShowSql();
    }

    @Override
    public DatabaseConverterManager getConverterManager() {
        return db.getConverterManager();
    }

    @Override
    public DatabaseDateConverter getDatabaseDateConverter() {
        return db.getDatabaseDateConverter();
    }

    @Override
    public void setShowSql(boolean enable) {
        db.setShowSql(enable);
    }


    /**
     * {@inheritDoc} This transaction is finished once it has been committed or rolled back, and every entry
     * point that could hand it work goes through here or through {@link #getConnection()}.
     */
    @Override
    public void validateUsable() {
        if (!isTransactionActive()) {
            throw new TransactionClosedException(this);
        }
    }

    @Override
    public Connection getConnection() {
        validateUsable();
        return conn;
    }

    @Override
    public void commit() {
        validateUsable();
        try {
            conn.commit();
        } catch (SQLException e) {
            throw new DaobabSQLException(e);
        } finally {
            setTransactionActive(false);
            db.closeConnectionPsychically(conn);
        }

    }

    /**
     * Rolls this transaction back and closes its connection. Deliberately the one operation that stays quiet on
     * an already finished transaction: it is cleanup, and the common {@code catch} block that calls it after a
     * {@code commit()} which itself threw must report the commit failure, not an exception about the
     * transaction being closed.
     */
    @Override
    public void rollback() {
        if (!isTransactionActive()) {
            //already finished by a commit() or an earlier rollback() - there is nothing left to undo, and the
            //connection is closed, so touching it would only mask whatever ended the transaction
            return;
        }
        try {
            conn.rollback();
        } catch (SQLException e) {
            throw new DaobabSQLException(e);
        } finally {
            setTransactionActive(false);
            db.closeConnectionPsychically(conn);
        }

    }


    /**
     * {@inheritDoc} Implemented as a JDBC {@link Savepoint} on this transaction's connection: the savepoint is
     * taken before the work, released when it succeeds and rolled back to when it fails - so a failed nested
     * transaction undoes only itself and this transaction stays open and usable.
     */
    @Override
    public <R> R wrapNestedTransaction(Supplier<R> work) {
        //throws when this transaction is already finished, before any savepoint is taken
        Connection connection = getConnection();
        Savepoint savepoint;
        try {
            savepoint = connection.setSavepoint();
        } catch (SQLException e) {
            throw new DaobabSQLException("Cannot start a nested transaction", e);
        }
        try {
            R rv = work.get();
            releaseSavepoint(connection, savepoint);
            return rv;
        } catch (RuntimeException e) {
            rollbackTo(connection, savepoint, e);
            throw e;
        }
    }

    private void rollbackTo(Connection connection, Savepoint savepoint, RuntimeException cause) {
        try {
            connection.rollback(savepoint);
        } catch (SQLException e) {
            //the reason the work failed stays the primary one, but a savepoint rollback that itself failed
            //leaves this transaction in an unknown state and must not disappear silently
            cause.addSuppressed(e);
        }
    }

    private void releaseSavepoint(Connection connection, Savepoint savepoint) {
        try {
            connection.releaseSavepoint(savepoint);
        } catch (SQLException | UnsupportedOperationException e) {
            //not every driver can release a savepoint (Oracle and Microsoft SQL Server cannot). The savepoint
            //goes away with the enclosing transaction anyway, so work that succeeded must not fail over this.
            getLog().debug("Cannot release the savepoint of a nested transaction: {}", e.getMessage());
        }
    }

    @Override
    public String getDataBaseProductName() {
        return db.getDataBaseProductName();
    }

    @Override
    public ResultSetReader getResultSetReader() {
        return db.getResultSetReader();
    }

    @Override
    public <E extends Entity> String toDeleteSqlQuery(DataBaseQueryDelete<E> base) {
        return db.toDeleteSqlQuery(base);
    }

    @Override
    public String withParameters(FrozenQueryProvider frozenQueryProvider, List<Object> injectionPointList) {
        return db.withParameters(frozenQueryProvider,injectionPointList);
    }

    @Override
    public <E extends Entity> QuerySpecialParameters toInsertSqlQuery(DataBaseQueryInsert<E> base) {
        return db.toInsertSqlQuery(base);
    }

    /**
     * Always fails: a transaction cannot be started on top of another one. Use {@link Propagation#NESTED} for a
     * nested transaction, or {@link Propagation#REQUIRED_NEW} for an independent one.
     *
     * @throws TransactionClosedException when this transaction is already finished
     * @throws TransactionOpenedException when it is still running
     */
    @Override
    public OpenTransactionDataBaseTargetImpl beginTransaction() {
        //a finished transaction is closed, not opened: reporting it as opened would send the caller looking for
        //a transaction that is no longer there
        validateUsable();
        throw new TransactionOpenedException();
    }

    @Override
    public List<Entity> getTables() {
        return db.getTables();
    }

    //Reads, DML and stored procedures are NOT delegated to the source target on purpose: the DataBaseTargetLogic
    //defaults take their connection from getConnection() below, which is this transaction's connection, and
    //ownsConnection() stops them from committing or closing it. Delegating them to db used to hand every
    //statement a fresh connection of its own, so it ran outside the transaction and rollback() could not undo it.

    @Override
    public <E extends Entity> String toSqlQuery(DataBaseQueryBase<E, ?> query) {
        return db.toSqlQuery(query);
    }

    @Override
    public <E extends Entity> String toSqlQuery(DataBaseQueryBase<E, ?> query, IdentifierStorage identifierStorage) {
        return db.toSqlQuery(query,identifierStorage);
    }

    @Override
    public DataSource getDataSource() {
        return db.getDataSource();
    }

    /**
     * {@inheritDoc} The target this transaction was opened on, so work that has to escape this transaction
     * (a {@code REQUIRED_NEW} or {@code NOT_SUPPORTED} propagation) gets a connection of its own instead of
     * this transaction's. Resolved transitively, so it is always a target with no transaction of its own.
     */
    @Override
    public TransactionalTarget getSourceTarget() {
        return db.getSourceTarget();
    }

    @Override
    public MetaData getMetaData() {
        return db.getMetaData();
    }

    @Override
    public AccessProtector getAccessProtector() {
        return db.getAccessProtector();
    }

    @Override
    public boolean isTransactionActive() {
        return transactionActive;
    }

    private void setTransactionActive(boolean transactionActive) {
        this.transactionActive = transactionActive;
    }

    @Override
    public <E extends Entity> QuerySpecialParameters toUpdateSqlQuery(DataBaseQueryUpdate<E> base) {
        return db.toUpdateSqlQuery(base);
    }

    @Override
    public String toCallProcedureSqlQuery(String procedureName, ProcedureParameters input, QueryTarget queryTarget) {
        return db.toCallProcedureSqlQuery(procedureName, input, queryTarget);
    }


    @Override
    public FrozenQueryBuffer getFrozenQueryBuffer() {
        return db.getFrozenQueryBuffer();
    }

    @Override
    public <E extends Entity> DataBaseIdGeneratorSupplier getPrimaryKeyGenerator(E entity) {
        return db.getPrimaryKeyGenerator(entity);
    }
}
