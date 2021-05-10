package io.daobab.target.database;

import io.daobab.error.DaobabSQLException;
import io.daobab.error.TransactionAlreadyOpened;
import io.daobab.error.TransactionClosedException;
import io.daobab.model.Entity;
import io.daobab.query.*;
import io.daobab.query.base.Query;
import io.daobab.query.base.QuerySpecialParameters;
import io.daobab.result.PlateBuffer;
import io.daobab.target.BaseTarget;
import io.daobab.target.OpenedTransactionTarget;
import io.daobab.target.QueryReceiver;
import io.daobab.target.QueryTarget;
import io.daobab.target.meta.MetaData;
import io.daobab.target.protection.AccessProtector;
import io.daobab.transaction.Propagation;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class OpenTransactionDataBaseTarget extends BaseTarget implements OpenedTransactionTarget, DataBaseTargetLogic, QueryReceiver {

    private final DataBaseTargetLogic db;
    private final Connection conn;
    private boolean transactionActive;


    //TODO: wywal baze danych zostaw czysty target transakcyjny
    public OpenTransactionDataBaseTarget(DataBaseTargetLogic target) {
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
    public <E extends Entity> String deleteQueryToExpression(QueryDelete<E> base) {
        return db.deleteQueryToExpression(base);
    }

    @Override
    public <E extends Entity> QuerySpecialParameters insertQueryToExpression(QueryInsert<E> base) {
        return db.insertQueryToExpression(base);
    }

    @Override
    public OpenTransactionDataBaseTarget beginTransaction() {
        throw new TransactionAlreadyOpened();
    }

    @Override
    public List<Entity> getTables() {
        return db.getTables();
    }

    @Override
    public <E extends Entity, F> F readField(QueryField<E, F> statementBase) {
        return db.readField(statementBase);
    }

    @Override
    public PlateBuffer readPlateList(QueryPlate statementBase) {
        return db.readPlateList(statementBase);
    }

    @Override
    public <E extends Entity> int delete(QueryDelete<E> query, boolean transaction) {
        return db.delete(query, transaction);
    }

    @Override
    public <E extends Entity> int delete(QueryDelete<E> query, Propagation propagation) {
        return db.delete(query, propagation);
    }

    @Override
    public <E extends Entity> int update(QueryUpdate<E> query, Propagation propagation) {
        return db.update(query, propagation);
    }

    @Override
    public <E extends Entity> E insert(QueryInsert<E> query, Propagation propagation) {
        return db.insert(query, propagation);
    }

    @Override
    public <E extends Entity> String toSqlQuery(Query<E, ?> query) {
        return db.toSqlQuery(query);
    }


    @Override
    public boolean isBuffer() {
        return db.isBuffer();
    }

    @Override
    public boolean isConnectedToDatabase() {
        return db.isConnectedToDatabase();
    }

    @Override
    public DataSource getDataSource() {
        return db.getDataSource();
    }

    @Override
    public TransactionalTarget getSourceTarget(){
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
    public <E extends Entity> QuerySpecialParameters toQueryUpdateExpression(QueryUpdate<E> base) {
        return db.toQueryUpdateExpression(base);
    }


}
