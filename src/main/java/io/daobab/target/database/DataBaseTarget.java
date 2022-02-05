package io.daobab.target.database;

import io.daobab.error.ColumnMandatory;
import io.daobab.error.DaobabException;
import io.daobab.error.DaobabSQLException;
import io.daobab.error.EntityMandatory;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.statement.where.WhereAnd;
import io.daobab.target.BaseTarget;
import io.daobab.target.QueryHandler;
import io.daobab.target.database.meta.MetaData;
import io.daobab.target.database.meta.MetaDataBaseTarget;
import io.daobab.target.database.meta.MetaDataTables;
import io.daobab.target.database.meta.table.MetaColumn;
import io.daobab.target.database.meta.table.MetaTable;
import io.daobab.target.database.query.DataBaseQueryDelete;
import io.daobab.target.database.query.DataBaseQueryInsert;
import io.daobab.target.database.query.DataBaseQueryUpdate;
import io.daobab.transaction.Propagation;
import io.daobab.transaction.TransactionIndicator;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public abstract class DataBaseTarget extends BaseTarget implements DataBaseTargetLogic, MetaDataTables {

    String dataBaseProductName;
    String dataBaseMajorVersion;
    String dataBaseMinorVersion;
    private List<Entity> tables = null;
    private DataSource dataSource;
    private MetaDataBaseTarget metaData;
    private String schemaName;
    private String catalogName;

    private boolean enabledLogQueries = false;


//    @Override
//    public  TransactionalTarget getSourceTarget(){
//        return  this;
//    }

    @Override
    public boolean isLogQueriesEnabled() {
        return enabledLogQueries;
    }

    public void setEnabledLogQueries(boolean enable) {
        this.enabledLogQueries = enable;
    }


    protected abstract DataSource initDataSource();

    protected abstract List<Entity> initTables();

    @Override
    public List<Entity> getTables() {
        if (tables == null) {
            tables = initTables();
        }
        return tables;
    }


    @Override
    public DataSource getDataSource() {
        if (dataSource == null) {
            UUID.randomUUID().toString(); //to init UUID
            this.dataSource = initDataSource();

            doSthOnConnection("", (x, c) -> {
                try {
                    setSchemaName(c.getSchema());
                    setCatalogName(c.getCatalog());
                } catch (SQLException e) {
                    throw new DaobabSQLException(e);
                }
                return null;
            });

            try {
                this.metaData = new MetaDataBaseTarget(getCatalogName(), getSchemaName(), this);
            } catch (SQLException throwables) {
                log.warn("DataBase Meta Specifics wasn't taken. ", throwables);
            }
            DaobabDataBaseMetaData meta = getDataBaseMetaData();

            getLog().info(String.format("Daobab DataBaseTarget %s connected to database %s - %s  Driver %s", this.getClass().getSimpleName(), meta.getDatabaseProductName(), meta.getDatabaseMajorVersion(), meta.getDriverName()));

            setDataBaseProductName(meta.getDatabaseProductName());
            setDataBaseMajorVersion(meta.getDatabaseMajorVersion());
            setDataBaseMinorVersion("" + meta.getDatabaseMinorVersion());


        }
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public String getDataBaseProductName() {
        if (dataBaseProductName == null) {
            getDataSource();
        }
        return dataBaseProductName;
    }

    public void setDataBaseProductName(String dataBaseProductName) {
        this.dataBaseProductName = dataBaseProductName;
    }

    public String getDataBaseMajorVersion() {
        return dataBaseMajorVersion;
    }

    public void setDataBaseMajorVersion(String dataBaseMajorVersion) {
        this.dataBaseMajorVersion = dataBaseMajorVersion;
    }

    public String getDataBaseMinorVersion() {
        return dataBaseMinorVersion;
    }

    public void setDataBaseMinorVersion(String dataBaseMinorVersion) {
        this.dataBaseMinorVersion = dataBaseMinorVersion;
    }

    public <E extends Entity> int delete(DataBaseQueryDelete<E> query, Propagation propagation) {
        return handleTransactionalTarget(this, propagation, (target, transaction) -> ((QueryDataBaseHandler) target).delete(query, transaction));
    }

    public <E extends Entity> int update(DataBaseQueryUpdate<E> query, Propagation propagation) {
        return handleTransactionalTarget(this, propagation, (target, transaction) -> ((QueryDataBaseHandler) target).update(query, transaction));
    }

    public <E extends Entity> E insert(DataBaseQueryInsert<E> query, Propagation propagation) {
        return handleTransactionalTarget(this, propagation, (target, transaction) -> ((QueryDataBaseHandler) target).insert(query, transaction));
    }

    public <Y, T extends TransactionalTarget> Y handleTransactionalTarget(T target, Propagation propagation, BiFunction<QueryHandler, Boolean, Y> jobToDo) {
        TransactionIndicator indicator = propagation.mayBeProceeded(target);
        switch (indicator) {
            case EXECUTE_WITHOUT: {
                return jobToDo.apply(target, false);
            }
            case START_NEW_JUST_FOR_IT: {
                return target.wrapTransaction(t -> jobToDo.apply(t.getSourceTarget(), true));
            }
            case GO_AHEAD: {
                return jobToDo.apply(target, true);
            }
        }
        throw new DaobabException("Problem related to specific propagation and transaction");
    }


    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public MetaData getMetaData() {
        if (metaData == null) {
            try {
                this.metaData = new MetaDataBaseTarget(getCatalogName(), getSchemaName(), this);
            } catch (SQLException sqlException) {
                log.warn("DataBase Meta Specifics wasn't taken. ", sqlException);
            }
        }
        return metaData;
    }

    public MetaColumn getMetaDataForColumn(Column<?, ?, ?> column) {
        if (column == null) throw new ColumnMandatory();
        return getMetaData().select(tabMetaColumn).where(new WhereAnd()
                        .equal(tabMetaColumn.colTableName(), column.getEntityName())
                        .equal(tabMetaColumn.colColumnName(), column.getColumnName()))
                .findOne();
    }

    public <E extends Entity> MetaTable getMetaDataForTable(E entity) {
        if (entity == null) throw new EntityMandatory();
        return getMetaData().select(tabMetaTable)
                .whereEqual(tabMetaTable.colTableName(), entity.getEntityName())
                .findOne();
    }


    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }


}
