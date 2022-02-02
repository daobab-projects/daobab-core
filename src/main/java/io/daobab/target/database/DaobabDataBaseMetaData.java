package io.daobab.target.database;

import io.daobab.target.buffer.single.Entities;
import io.daobab.target.database.meta.table.*;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class DaobabDataBaseMetaData {

    private int maxBinaryLiteralLength;
    private int maxCatalogNameLength;

    private int maxCharLiteralLength;
    private int maxColumnNameLength;
    private int maxColumnsInTable;
    private int maxSchemaNameLength;
    private int maxStatementLength;
    private int maxRowSize;
    private int maxTableNameLength;
    private int maxTablesInSelect;
    private boolean numericFunctions;
    private boolean stringFunctions;
    private boolean systemFunctions;
    private boolean timeDateFunctions;

    private String databaseMajorVersion;
    private int databaseMinorVersion;
    private String databaseProductName;
    private String databaseProductVersion;
    private String driverName;
    private String driverVersion;
    private int maxConnections;
    private Entities<MetaTable> tables;
    private Entities<MetaColumn> columns;
    private Entities<MetaPrimaryKey> primaryKeys;
    private Entities<MetaSchema> schemas;
    private Entities<MetaCatalog> catalogs;

    public Entities<MetaTable> getTables() {
        return tables;
    }

    public void setTables(Entities<MetaTable> tables) {
        this.tables = tables;
    }

    public Entities<MetaColumn> getColumns() {
        return columns;
    }

    public void setColumns(Entities<MetaColumn> columns) {
        this.columns = columns;
    }

    public String getDatabaseMajorVersion() {
        return databaseMajorVersion;
    }

    public void setDatabaseMajorVersion(String databaseMajorVersion) {
        this.databaseMajorVersion = databaseMajorVersion;
    }

    public String getDatabaseProductName() {
        return databaseProductName;
    }

    public void setDatabaseProductName(String databaseProductName) {
        this.databaseProductName = databaseProductName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverVersion() {
        return driverVersion;
    }

    public void setDriverVersion(String driverVersion) {
        this.driverVersion = driverVersion;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public Entities<MetaPrimaryKey> getPrimaryKeys() {
        return primaryKeys;
    }

    public void setPrimaryKeys(Entities<MetaPrimaryKey> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    public Entities<MetaSchema> getSchemas() {
        return schemas;
    }

    public void setSchemas(Entities<MetaSchema> schemas) {
        this.schemas = schemas;
    }

    public Entities<MetaCatalog> getCatalogs() {
        return catalogs;
    }

    public void setCatalogs(Entities<MetaCatalog> catalogs) {
        this.catalogs = catalogs;
    }


    public int getMaxBinaryLiteralLength() {
        return maxBinaryLiteralLength;
    }

    public void setMaxBinaryLiteralLength(int maxBinaryLiteralLength) {
        this.maxBinaryLiteralLength = maxBinaryLiteralLength;
    }

    public int getMaxCatalogNameLength() {
        return maxCatalogNameLength;
    }

    public void setMaxCatalogNameLength(int maxCatalogNameLength) {
        this.maxCatalogNameLength = maxCatalogNameLength;
    }

    public int getMaxCharLiteralLength() {
        return maxCharLiteralLength;
    }

    public void setMaxCharLiteralLength(int maxCharLiteralLength) {
        this.maxCharLiteralLength = maxCharLiteralLength;
    }

    public int getMaxColumnNameLength() {
        return maxColumnNameLength;
    }

    public void setMaxColumnNameLength(int maxColumnNameLength) {
        this.maxColumnNameLength = maxColumnNameLength;
    }

    public int getMaxColumnsInTable() {
        return maxColumnsInTable;
    }

    public void setMaxColumnsInTable(int maxColumnsInTable) {
        this.maxColumnsInTable = maxColumnsInTable;
    }

    public int getMaxSchemaNameLength() {
        return maxSchemaNameLength;
    }

    public void setMaxSchemaNameLength(int maxSchemaNameLength) {
        this.maxSchemaNameLength = maxSchemaNameLength;
    }

    public int getMaxStatementLength() {
        return maxStatementLength;
    }

    public void setMaxStatementLength(int maxStatementLength) {
        this.maxStatementLength = maxStatementLength;
    }

    public int getMaxRowSize() {
        return maxRowSize;
    }

    public void setMaxRowSize(int maxRowSize) {
        this.maxRowSize = maxRowSize;
    }

    public int getMaxTableNameLength() {
        return maxTableNameLength;
    }

    public void setMaxTableNameLength(int maxTableNameLength) {
        this.maxTableNameLength = maxTableNameLength;
    }

    public int getMaxTablesInSelect() {
        return maxTablesInSelect;
    }

    public void setMaxTablesInSelect(int maxTablesInSelect) {
        this.maxTablesInSelect = maxTablesInSelect;
    }

    public boolean isNumericFunctions() {
        return numericFunctions;
    }

    public void setNumericFunctions(boolean numericFunctions) {
        this.numericFunctions = numericFunctions;
    }

    public boolean isStringFunctions() {
        return stringFunctions;
    }

    public void setStringFunctions(boolean stringFunctions) {
        this.stringFunctions = stringFunctions;
    }

    public boolean isSystemFunctions() {
        return systemFunctions;
    }

    public void setSystemFunctions(boolean systemFunctions) {
        this.systemFunctions = systemFunctions;
    }

    public boolean isTimeDateFunctions() {
        return timeDateFunctions;
    }

    public void setTimeDateFunctions(boolean timeDateFunctions) {
        this.timeDateFunctions = timeDateFunctions;
    }

    public int getDatabaseMinorVersion() {
        return databaseMinorVersion;
    }

    public void setDatabaseMinorVersion(int databaseMinorVersion) {
        this.databaseMinorVersion = databaseMinorVersion;
    }

    public String getDatabaseProductVersion() {
        return databaseProductVersion;
    }

    public void setDatabaseProductVersion(String databaseProductVersion) {
        this.databaseProductVersion = databaseProductVersion;
    }

}
