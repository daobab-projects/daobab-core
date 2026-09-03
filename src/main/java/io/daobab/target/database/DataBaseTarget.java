package io.daobab.target.database;

import io.daobab.dict.DictDatabaseType;
import io.daobab.error.DaobabSQLException;
import io.daobab.error.MandatoryColumn;
import io.daobab.error.MandatoryEntity;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.statement.where.WhereAnd;
import io.daobab.target.BaseTarget;
import io.daobab.target.database.connection.JDBCResultSetReader;
import io.daobab.target.database.connection.ResultSetReader;
import io.daobab.target.database.converter.DatabaseConverterManager;
import io.daobab.target.database.converter.dateformat.*;
import io.daobab.target.database.meta.MetaData;
import io.daobab.target.database.meta.MetaDataBaseTarget;
import io.daobab.target.database.meta.MetaDataTables;
import io.daobab.target.database.meta.table.MetaColumn;
import io.daobab.target.database.meta.table.MetaTable;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class DataBaseTarget extends BaseTarget implements DataBaseTargetLogic, MetaDataTables, FrozenQueryBufferProvider {

    String dataBaseProductName;
    String dataBaseMajorVersion;
    String dataBaseMinorVersion;
    private List<Entity> tables = null;
    private DataSource dataSource;
    private MetaDataBaseTarget metaData;
    private String schemaName;
    private String catalogName;
    private boolean sql = false;
    private final DatabaseConverterManager converterManager;
    private final ResultSetReader resultSetReader;
    private DatabaseDateConverter databaseDateConverter;
    private final FrozenQueryBuffer frozenQueryBuffer;

    protected DataBaseTarget() {
        converterManager = new DatabaseConverterManager(this);
        resultSetReader = new JDBCResultSetReader();
        frozenQueryBuffer = new FrozenQueryBuffer();
    }


    public boolean isConnectedToDatabase() {
        return true;
    }


    @Override
    public boolean getShowSql() {
        return sql;
    }

    @Override
    public void setShowSql(boolean enable) {
        this.sql = enable;
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
            setDataBaseMinorVersion(String.valueOf(meta.getDatabaseMinorVersion()));

            if (DictDatabaseType.ORACLE.equals(meta.getDatabaseProductName())) {
                setDatabaseDateConverter(new DatabaseDateConverterOracle());
            } else if (meta.getDatabaseProductName().startsWith(DictDatabaseType.MicrosoftSQL)) {
                setDatabaseDateConverter(new DatabaseDateConverterMicrosoftSql());
            } else if (DictDatabaseType.MYSQL.equals(meta.getDatabaseProductName())) {
                setDatabaseDateConverter(new DatabaseDateConverterMySql());
            } else if (DictDatabaseType.PostgreSQL.equals(meta.getDatabaseProductName())) {
                setDatabaseDateConverter(new DatabaseDateConverterPostgreSql());
            } else if (DictDatabaseType.H2.equals(meta.getDatabaseProductName())) {
                setDatabaseDateConverter(new DatabaseDateConverterH2());
            } else {
                log.error("No data converter for a database type: {}. Set the correct DatabaseDateConverter!", meta.getDatabaseProductName());
                setDatabaseDateConverter(new DatabaseDateConverterH2());
            }


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

    //delete/update/insert under a Propagation and handleTransactionalTarget are inherited: they are defaulted in
    //DataBaseTargetLogic and TransactionalTarget, so a plain target and an open transaction share one implementation

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
        if (column == null) throw new MandatoryColumn();
        return getMetaData().select(tabMetaColumn).where(new WhereAnd()
                        .equal(tabMetaColumn.colTableName(), getEntityName(column.entityClass()))
                        .equal(tabMetaColumn.colColumnName(), column.getColumnName()))
                .findOne();
    }

    public <E extends Entity> MetaTable getMetaDataForTable(E entity) {
        if (entity == null) throw new MandatoryEntity();
        return getMetaData().select(tabMetaTable)
                .whereEqual(tabMetaTable.colTableName(), getEntityName(entity.entityClass()))
                .findOne();
    }


    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    @Override
    public ResultSetReader getResultSetReader() {
        return resultSetReader;
    }

    @Override
    public DatabaseConverterManager getConverterManager() {
        return converterManager;
    }

    public DatabaseDateConverter getDatabaseDateConverter() {
        return databaseDateConverter;
    }

    public void setDatabaseDateConverter(DatabaseDateConverter databaseDateConverter) {
        this.databaseDateConverter = databaseDateConverter;
    }


    @Override
    public FrozenQueryBuffer getFrozenQueryBuffer() {
        return frozenQueryBuffer;
    }
}
