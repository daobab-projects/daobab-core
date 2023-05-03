package io.daobab.target.database.meta;

import io.daobab.generator.JDBCTypeConverter;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.target.buffer.multi.AboveMultiEntityTarget;
import io.daobab.target.buffer.single.Entities;
import io.daobab.target.buffer.single.EntityList;
import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.connection.JdbcType;
import io.daobab.target.database.meta.column.dict.MetaRule;
import io.daobab.target.database.meta.table.*;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class MetaDataBaseTarget extends AboveMultiEntityTarget implements MetaData, MetaDataTables {

    private final DataBaseTarget source;
    private final HashMap<String, MetaTable> quickAccessMetaTable = new HashMap<>();
    private final HashMap<String, MetaColumn> quickAccessMetaColumn = new HashMap<>();

    public MetaSchema tabMetaSchema = new MetaSchema();
    public MetaTable tabMetaTable = new MetaTable();
    public MetaColumn tabMetaColumn = new MetaColumn();
    public MetaPrimaryKey tabMetaPrimaryKey = new MetaPrimaryKey();
    public MetaForeignKey tabMetaForeignKey = new MetaForeignKey();
    public MetaIndex tabMetaIndex = new MetaIndex();

    public MetaDataBaseTarget(String catalog, String schema, DataBaseTarget source) throws SQLException {
        if (schema == null) {
            schema = "%";
        }
        if (catalog == null) {
            catalog = "%";
        }

        JDBCTypeConverter typeConverter = new JDBCTypeConverter();
        this.source = source;
        register(MetaTable.class, MetaColumn.class);

        List<MetaTable> tables = new ArrayList<>();
        List<MetaColumn> columns = new ArrayList<>();
        List<MetaIndex> indexes = new ArrayList<>();
        List<MetaForeignKey> foreignKeys = new ArrayList<>();
        List<MetaPrimaryKey> primaryKeys = new ArrayList<>();

        DatabaseMetaData databaseMetaData = getSourceTarget().getDataSource().getConnection().getMetaData();


        ResultSet rsTable = databaseMetaData.getTables(catalog, schema, "%", new String[]{"TABLE", "VIEW"});

        while (rsTable.next()) {
            MetaTable mt = new MetaTable();
            mt.setTableName(rsTable.getString("TABLE_NAME"));
            mt.setRemarks(rsTable.getString("REMARKS"));
            mt.setTableType(rsTable.getString("TABLE_TYPE"));

            String primaryKeyColumn = null;

            ResultSet rsPk = databaseMetaData.getPrimaryKeys(catalog, schema, mt.getTableName());
            while (rsPk.next()) {
                primaryKeyColumn = rsPk.getString("COLUMN_NAME");
            }

            foreignKeys.addAll(readForeignKeys(databaseMetaData.getExportedKeys(catalog, schema, mt.getTableName())));
            primaryKeys.addAll(readPrimaryKeys(databaseMetaData.getPrimaryKeys(catalog, schema, mt.getTableName())));
            indexes.addAll(readIndexes(databaseMetaData.getIndexInfo(catalog, schema, mt.getTableName(), false, false)));

            ResultSet rsColumn = databaseMetaData.getColumns(catalog, schema, mt.getTableName(), "%");

            int counter = 0;
            while (rsColumn.next()) {
                MetaColumn mc = new MetaColumn();
                String columnName = rsColumn.getString("COLUMN_NAME");
                mc.setColumnName(columnName.contains(" ") ? "`" + columnName + "`" : columnName);
//            mc.setPrimaryKey(mc.getColumnName().equals(primaryKeyColumn));
                mc.setColumnSize(rsColumn.getInt("COLUMN_SIZE"));
                mc.setDecimalDigits(rsColumn.getInt("DECIMAL_DIGITS"));
                mc.setNullable(rsColumn.getBoolean("NULLABLE"));
                mc.setRemarks(rsColumn.getString("REMARKS"));
                mc.setDatatype(JdbcType.valueOf(rsColumn.getInt("DATA_TYPE")));
                mc.setTableName(rsColumn.getString("TABLE_NAME"));
                mc.setFieldClass(typeConverter.convert(JDBCTypeConverter.UNKNOWN_TABLE, rsColumn.getInt("DATA_TYPE")));
                mc.setColumnDefault(rsColumn.getString(mc.colColumnDefault().getColumnName()));
                mc.setOrdinalPosition(rsColumn.getInt(mc.colOrdinalPosition().getColumnName()));
                mc.setTableColumnName(mc.getTableName() + "." + mc.getColumnName());
                columns.add(mc);

                counter++;
            }

            mt.setColumnCount(counter);
            tables.add(mt);
        }

        tables.forEach(t -> quickAccessMetaTable.put(t.getTableName(), t));
        columns.forEach(t -> quickAccessMetaColumn.put(t.getTableColumnName(), t));

        put(new EntityList<>(tables, MetaTable.class),
                new EntityList<>(columns, MetaColumn.class),
                new EntityList<>(foreignKeys, MetaForeignKey.class),
                new EntityList<>(primaryKeys, MetaPrimaryKey.class),
                new EntityList<>(indexes, MetaIndex.class),
                getTargetSchemas());
    }

    private EntityList<MetaSchema> getTargetSchemas() throws SQLException {
        DatabaseMetaData databaseMetaData = getSourceTarget().getDataSource().getConnection().getMetaData();

        ResultSet rs = databaseMetaData.getSchemas();
        List<MetaSchema> schemas = new ArrayList<>();

        while (rs.next()) {
            MetaSchema schema = new MetaSchema();
            schema.setCatalogName(rs.getString("TABLE_CATALOG"));
            schema.setSchemaName(rs.getString("TABLE_SCHEM"));

            schemas.add(schema);
        }
        return new EntityList<>(schemas, MetaSchema.class);
    }


    private List<MetaIndex> readIndexes(ResultSet rs) throws SQLException {
        List<MetaIndex> indexList = new ArrayList<>();
        while (rs.next()) {
            MetaIndex index = new MetaIndex();
            index.setCatalogName(rs.getString("TABLE_CAT"));
            index.setSchemaName(rs.getString("TABLE_SCHEM"));
            index.setTableName(rs.getString("TABLE_NAME"));
            index.setIndexName(rs.getString("INDEX_NAME"));
            index.setIndexType(rs.getString("TYPE"));

            index.setColumnName(rs.getString("COLUMN_NAME"));
            index.setAscending(rs.getString("ASC_OR_DESC"));
            index.setCardinality(rs.getInt("CARDINALITY"));

            index.setTableColumnName(index.getTableName() + "." + index.getColumnName());
            indexList.add(index);
        }

        return indexList;
    }


    private List<MetaPrimaryKey> readPrimaryKeys(ResultSet rs) throws SQLException {
        List<MetaPrimaryKey> metaPrimaryKeys = new ArrayList<>();
        while (rs.next()) {
            MetaPrimaryKey primaryKey = new MetaPrimaryKey();
            primaryKey.setCatalogName(rs.getString("TABLE_CAT"));
            primaryKey.setSchemaName(rs.getString("TABLE_SCHEM"));
            primaryKey.setTableName(rs.getString("TABLE_NAME"));
            primaryKey.setColumnName(rs.getString("COLUMN_NAME"));
            primaryKey.setPkName(rs.getString("PK_NAME"));
            primaryKey.setKeySeq(rs.getString("KEY_SEQ"));
            primaryKey.setTableColumnName(primaryKey.getTableName() + "." + primaryKey.getColumnName());
            metaPrimaryKeys.add(primaryKey);
        }

        return metaPrimaryKeys;
    }

    private List<MetaForeignKey> readForeignKeys(ResultSet rs) throws SQLException {
        List<MetaForeignKey> foreignKeyList = new ArrayList<>();
        while (rs.next()) {

            MetaForeignKey foreignKey = new MetaForeignKey();

            foreignKey.setPkCatalogName(rs.getString("PKTABLE_CAT"));
            foreignKey.setPkSchemaName(rs.getString("PKTABLE_SCHEM"));
            foreignKey.setPkTableName(rs.getString("PKTABLE_NAME"));
            foreignKey.setPkColumnName(rs.getString("PKCOLUMN_NAME"));
            foreignKey.setPkName(rs.getString("PK_NAME"));
            foreignKey.setFkCatalogName(rs.getString("FKTABLE_CAT"));
            foreignKey.setFkSchemaName(rs.getString("FKTABLE_SCHEM"));
            foreignKey.setFkTableName(rs.getString("FKTABLE_NAME"));
            foreignKey.setFkColumnName(rs.getString("FKCOLUMN_NAME"));
            foreignKey.setFkName(rs.getString("FK_NAME"));
            foreignKey.setKeySeq(rs.getString("KEY_SEQ"));
            foreignKey.setUpdateRule(MetaRule.ofString(rs.getString("UPDATE_RULE")));
            foreignKey.setDeleteRule(MetaRule.ofString(rs.getString("DELETE_RULE")));

            foreignKeyList.add(foreignKey);
        }

        return foreignKeyList;
    }

    @Override
    protected DataBaseTarget getSourceTarget() {
        return source;
    }

    @Override
    public Entities<MetaTable> getMetaTables() {
        return new EntityList<>(new ArrayList<>(quickAccessMetaTable.values()), MetaTable.class);
    }

    @Override
    public Entities<MetaSchema> getMetaSchemas() {
        return select(tabMetaSchema).findMany();
    }

    @Override
    public Entities<MetaColumn> getMetaColumns() {
        return new EntityList<>(new ArrayList<>(quickAccessMetaColumn.values()), MetaColumn.class);
    }

    @Override
    public MetaEntity getMetaEntityFor(MetaTable metaTable) {
        return new MetaEntity(metaTable.getTableName(),
                select(tabMetaColumn)
                        .whereEqual(tabMetaColumn.colTableName(), metaTable)
                        .findMany());
    }

    @SuppressWarnings("rawtypes")
    public Optional<MetaColumn> getMetaColumn(Column column) {
        return Optional.ofNullable(quickAccessMetaColumn.get(column.getEntityName() + "." + column.getColumnName()));
    }

    public Optional<MetaTable> getMetaTable(Entity entity) {
        return Optional.ofNullable(quickAccessMetaTable.get(entity.getEntityName()));
    }


}
