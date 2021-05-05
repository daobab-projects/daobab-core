package io.daobab.generator;

import io.daobab.result.Entities;
import io.daobab.result.EntityList;
import io.daobab.target.database.DaobabDataBaseMetaData;
import io.daobab.target.meta.table.*;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class ImageExporter {
    DataSource ds;

    public ImageExporter(DataSource ds) {
        Connection c = null;
        Entities<MetaTable> tables = null;
        Entities<MetaColumn> columns = null;
        Entities<MetaSchema> schemas = null;
        Entities<MetaCatalog> catalogs = null;
        Entities<MetaPrimaryKey> primaryKeys = null;
        try {
            c = ds.getConnection();
            this.ds = ds;

            DaobabDataBaseMetaData rv = new DaobabDataBaseMetaData();

            try {
                rv.setDatabaseMajorVersion("" + c.getMetaData().getDatabaseMajorVersion());
                rv.setDatabaseProductName(c.getMetaData().getDatabaseProductName());
                rv.setDriverName(c.getMetaData().getDriverName());
                rv.setDriverVersion(c.getMetaData().getDriverVersion());
                rv.setMaxConnections(c.getMetaData().getMaxConnections());

                rv.setDatabaseMinorVersion(c.getMetaData().getDatabaseMinorVersion());
                rv.setDatabaseProductVersion(c.getMetaData().getDatabaseProductVersion());
                rv.setMaxConnections(c.getMetaData().getMaxConnections());
                rv.setMaxBinaryLiteralLength(c.getMetaData().getMaxBinaryLiteralLength());
                rv.setMaxCatalogNameLength(c.getMetaData().getMaxCatalogNameLength());
                rv.setMaxCharLiteralLength(c.getMetaData().getMaxCharLiteralLength());
                rv.setMaxColumnNameLength(c.getMetaData().getMaxColumnNameLength());
                rv.setMaxColumnsInTable(c.getMetaData().getMaxColumnsInTable());
                rv.setMaxSchemaNameLength(c.getMetaData().getMaxSchemaNameLength());
                rv.setMaxStatementLength(c.getMetaData().getMaxStatementLength());
                rv.setMaxRowSize(c.getMetaData().getMaxRowSize());
                rv.setMaxTableNameLength(c.getMetaData().getMaxTableNameLength());
                rv.setMaxTablesInSelect(c.getMetaData().getMaxTablesInSelect());
                rv.setNumericFunctions("YES".equalsIgnoreCase(c.getMetaData().getNumericFunctions()));
                rv.setStringFunctions("YES".equalsIgnoreCase(c.getMetaData().getStringFunctions()));
                rv.setSystemFunctions("YES".equalsIgnoreCase(c.getMetaData().getSystemFunctions()));
                rv.setTimeDateFunctions("YES".equalsIgnoreCase(c.getMetaData().getTimeDateFunctions()));

                tables = readTables(c.getMetaData().getTables(null, null, "%", new String[]{"TABLE", "VIEW"}));
                columns = readColumns(c.getMetaData().getColumns(null, null, null, "%"));
                primaryKeys = readPrimaryKeys(c.getMetaData().getPrimaryKeys(null, null, "%"));
                schemas = readSchemas(c.getMetaData().getSchemas());
                catalogs = readCatalogs(c.getMetaData().getCatalogs());

                rv.setTables(tables);
                rv.setColumns(columns);
                rv.setPrimaryKeys(primaryKeys);
                rv.setSchemas(schemas);
                rv.setCatalogs(catalogs);

                System.out.println("Connection OK.Database: " + rv.getDatabaseProductName() + " version:" + rv.getDatabaseMajorVersion() + "." + rv.getDatabaseMajorVersion());

            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println(columns.toJSON());
//        System.out.println(tables.toJSON());
    }

    private Entities<MetaTable> readTables(ResultSet rs) throws SQLException {
        List<MetaTable> metaTables = new LinkedList<>();
        while (rs.next()) {
            MetaTable table = new MetaTable();
            table.setTableName(rs.getString("TABLE_NAME"));
            table.setSchemaName(rs.getString("TABLE_SCHEM"));
            table.setCatalogName(rs.getString("TABLE_CAT"));
//                    generateTable.setSchemaName(s.getSchema());
            table.setRemarks(rs.getString("REMARKS"));
//            String type=rs.getString("TABLE_TYPE");
//            generateTable.setType();
//            if (s.canBeGenerated(generateTable.getTableName())) tables.add(generateTable);
            metaTables.add(table);
        }

        return new EntityList<>(metaTables, MetaTable.class);
    }

    private Entities<MetaColumn> readColumns(ResultSet rs) throws SQLException {
        List<MetaColumn> metaColumns = new LinkedList<>();
        while (rs.next()) {
            MetaColumn column = new MetaColumn();
            column.setColumnName(rs.getString("COLUMN_NAME"));
            column.setTableName(rs.getString("TABLE_NAME"));
            column.setSchemaName(rs.getString("TABLE_SCHEM"));
            column.setCatalogName(rs.getString("TABLE_CAT"));
            column.setColumnDefault(rs.getString("COLUMN_DEF"));
//            generateTable.setColumnSize(rs.getString("TABLE_SCHEM"));
//                    generateTable.setSchemaName(s.getSchema());
            column.setRemarks(rs.getString("REMARKS"));
            column.setColumnSize(rs.getInt("COLUMN_SIZE"));
            column.setDecimalDigits(rs.getInt("DECIMAL_DIGITS"));
            int nul = rs.getInt("NULLABLE");
            if (nul == 3) {
                column.setNullable(null);
            } else if (nul == 0) {
                column.setNullable(false);
            } else {
                column.setNullable(true);
            }

            metaColumns.add(column);
        }

        return new EntityList<>(metaColumns, MetaColumn.class);
    }


    private Entities<MetaPrimaryKey> readPrimaryKeys(ResultSet rs) throws SQLException {
        List<MetaPrimaryKey> metaPrimaryKeys = new LinkedList<>();
        while (rs.next()) {
            MetaPrimaryKey primaryKey = new MetaPrimaryKey();
            primaryKey.setCatalogName(rs.getString("TABLE_CAT"));
            primaryKey.setSchemaName(rs.getString("TABLE_SCHEM"));
            primaryKey.setTableName(rs.getString("TABLE_NAME"));
            primaryKey.setColumnName(rs.getString("COLUMN_NAME"));
            primaryKey.setPkName(rs.getString("PK_NAME"));
            primaryKey.setKeySeq(rs.getString("KEY_SEQ"));

            metaPrimaryKeys.add(primaryKey);
        }

        return new EntityList<>(metaPrimaryKeys, MetaPrimaryKey.class);
    }

    private Entities<MetaSchema> readSchemas(ResultSet rs) throws SQLException {
        List<MetaSchema> schemas = new LinkedList<>();
        while (rs.next()) {
            MetaSchema schema = new MetaSchema();
            schema.setCatalogName(rs.getString("TABLE_CAT"));
            schema.setSchemaName(rs.getString("TABLE_SCHEM"));

            schemas.add(schema);
        }

        return new EntityList<>(schemas, MetaSchema.class);
    }


    private Entities<MetaCatalog> readCatalogs(ResultSet rs) throws SQLException {
        List<MetaCatalog> schemas = new LinkedList<>();
        while (rs.next()) {
            MetaCatalog schema = new MetaCatalog();
            schema.setCatalogName(rs.getString("TABLE_CAT"));

            schemas.add(schema);
        }

        return new EntityList<>(schemas, MetaCatalog.class);
    }

}
