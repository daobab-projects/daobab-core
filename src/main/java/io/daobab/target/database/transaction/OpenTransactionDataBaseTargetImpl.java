package io.daobab.target.database.transaction;

import io.daobab.error.DaobabSQLException;
import io.daobab.error.TransactionAlreadyOpened;
import io.daobab.error.TransactionClosedException;
import io.daobab.model.Entity;
import io.daobab.model.ProcedureParameters;
import io.daobab.query.base.QuerySpecialParameters;
import io.daobab.target.BaseTarget;
import io.daobab.target.buffer.single.PlateBuffer;
import io.daobab.target.database.DataBaseTargetLogic;
import io.daobab.target.database.QueryDataBaseHandler;
import io.daobab.target.database.TransactionalTarget;
import io.daobab.target.database.meta.MetaData;
import io.daobab.target.database.query.*;
import io.daobab.target.protection.AccessProtector;
import io.daobab.transaction.Propagation;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
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
    public boolean isLogQueriesEnabled() {
        return db.isLogQueriesEnabled();
    }


    @Override
    public Connection getConnection() {
        if (!isTransactionActive()) {
            throw new TransactionClosedException(this);
        }
        return conn;
    }

    @Override
    public void commit() {
        if (!isTransactionActive()) {
            throw new TransactionClosedException(this);
        }
        try {
            conn.commit();
        } catch (SQLException e) {
            throw new DaobabSQLException(e);
        } finally {
            setTransactionActive(false);
            db.closeConnectionPsychically(conn);
        }

    }

    @Override
    public void rollback() {
        try {
            conn.rollback();
        } catch (SQLException e) {
            throw new DaobabSQLException(e);
        } finally {
            setTransactionActive(false);
            db.closeConnectionPsychically(conn);
        }

    }


    @Override
    public String getDataBaseProductName() {
        return db.getDataBaseProductName();
    }

    @Override
    public <E extends Entity> String toDeleteSqlQuery(DataBaseQueryDelete<E> base) {
        return db.toDeleteSqlQuery(base);
    }

    @Override
    public <E extends Entity> QuerySpecialParameters toInsertSqlQuery(DataBaseQueryInsert<E> base) {
        return db.toInsertSqlQuery(base);
    }

    @Override
    public OpenTransactionDataBaseTargetImpl beginTransaction() {
        throw new TransactionAlreadyOpened();
    }

    @Override
    public List<Entity> getTables() {
        return db.getTables();
    }

    @Override
    public <E extends Entity, F> F readField(DataBaseQueryField<E, F> statementBase) {
        return db.readField(statementBase);
    }

    @Override
    public PlateBuffer readPlateList(DataBaseQueryPlate statementBase) {
        return db.readPlateList(statementBase);
    }

    @Override
    public <E extends Entity> int delete(DataBaseQueryDelete<E> query, boolean transaction) {
        return db.delete(query, transaction);
    }

    @Override
    public <E extends Entity> int delete(DataBaseQueryDelete<E> query, Propagation propagation) {
        return db.delete(query, propagation);
    }

    @Override
    public <E extends Entity> int update(DataBaseQueryUpdate<E> query, Propagation propagation) {
        return db.update(query, propagation);
    }

    @Override
    public <E extends Entity> E insert(DataBaseQueryInsert<E> query, Propagation propagation) {
        return db.insert(query, propagation);
    }

    @Override
    public <E extends Entity> String toSqlQuery(DataBaseQueryBase<E, ?> query) {
        return db.toSqlQuery(query);
    }

    @Override
    public <O extends ProcedureParameters, I extends ProcedureParameters> O callProcedure(String name, I in, O out) {
        return db.callProcedure(name, in, out);
    }

    @Override
    public DataSource getDataSource() {
        return db.getDataSource();
    }

    //    @Override
    public TransactionalTarget getSourceTarget() {
        return db;
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
    public String toCallProcedureSqlQuery(String procedureName, ProcedureParameters input) {
        return db.toCallProcedureSqlQuery(procedureName, input);
    }


}
