package io.daobab.target.database;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.result.Entities;
import io.daobab.result.EntityList;
import io.daobab.target.meta.column.dict.MetaRule;
import io.daobab.target.meta.table.MetaForeignKey;
import io.daobab.target.meta.table.MetaIndex;
import io.daobab.target.multi.AboveMultiEntityTarget;
import io.daobab.target.meta.MetaData;
import io.daobab.target.meta.table.MetaColumn;
import io.daobab.target.meta.table.MetaTable;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MetaDataBaseTarget extends AboveMultiEntityTarget implements MetaData {

    private DataBaseTarget source;
    private HashMap<String, MetaTable> quickAccessMetaTable = new HashMap<>();
    private HashMap<String, MetaColumn> quickAccessMetaColumn = new HashMap<>();

    public MetaDataBaseTarget(String catalog, String schema, DataBaseTarget source) throws SQLException {
        if (schema == null) {
            schema = "%";
        }
        if (catalog == null) {
            catalog = "%";
        }
        this.source = source;
        register(MetaTable.class, MetaColumn.class);

        List<MetaTable> tables = new ArrayList<>();
        List<MetaColumn> columns = new ArrayList<>();

        DatabaseMetaData databaseMetaData = getSourceTarget().getDataSource().getConnection().getMetaData();

        ResultSet rsTable = databaseMetaData.getTables(catalog, schema, "%", new String[]{"TABLE", "VIEW"});

        while (rsTable.next()) {
            MetaTable mt = new MetaTable();
            mt.setTableName(rsTable.getString("TABLE_NAME"));
            mt.setRemarks(rsTable.getString("REMARKS"));
//            mt.setTableType(rs.getString("TABLE_TYPE"));

            String primaryKeyColumn = null;

            ResultSet rsPk = databaseMetaData.getPrimaryKeys(catalog, schema, mt.getTableName());
            while (rsPk.next()) {
                primaryKeyColumn = rsPk.getString("COLUMN_NAME");
            }

            ResultSet rsColumn = databaseMetaData.getColumns(catalog, schema, mt.getTableName(), "%");

            int counter = 0;
            while (rsColumn.next()) {
                MetaColumn mc = new MetaColumn();
                mc.setColumnName(rsColumn.getString("COLUMN_NAME"));
//            mc.setPrimaryKey(mc.getColumnName().equals(primaryKeyColumn));
                mc.setColumnSize(rsColumn.getInt("COLUMN_SIZE"));
                mc.setDecimalDigits(rsColumn.getInt("DECIMAL_DIGITS"));
                mc.setNullable(rsColumn.getBoolean("NULLABLE"));
                mc.setRemarks(rsColumn.getString("REMARKS"));
                mc.setDatatype(JdbcType.valueOf(rsColumn.getInt("DATA_TYPE")));
                mc.setTableName(rsColumn.getString("TABLE_NAME"));
                mc.setColumnDefault(rsColumn.getString(mc.colColumnDefault().getColumnName()));
                mc.setOrdinalPosition(rsColumn.getInt(mc.colOrdinalPosition().getColumnName()));
                mc.setTableColumnName(mc.getTableName() + "." + mc.getColumnName());
                columns.add(mc);
                counter++;
            }

            mt.setColumnCount(counter);
            tables.add(mt);
        }

        tables.forEach(t -> quickAccessMetaTable.put(t.getEntityName(), t));
        columns.forEach(t -> quickAccessMetaColumn.put(t.getTableColumnName(), t));

        put(new EntityList<>(tables, MetaTable.class),
                new EntityList<>(columns, MetaColumn.class),
                readIndexes(databaseMetaData.getIndexInfo(catalog, schema, "%", false,false)),
                readForeignKeys(databaseMetaData.getExportedKeys(catalog, schema, "%")));
    }


    private Entities<MetaIndex> readIndexes(ResultSet rs) throws SQLException {
        List<MetaIndex> indexList = new LinkedList<>();
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

        return new EntityList<>(indexList, MetaIndex.class);
    }

    private Entities<MetaForeignKey> readForeignKeys(ResultSet rs) throws SQLException {
        List<MetaForeignKey> foreignKeyList = new LinkedList<>();
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

        return new EntityList<>(foreignKeyList, MetaForeignKey.class);
    }

    @Override
    protected DataBaseTarget getSourceTarget() {
        return source;
    }

    public Optional<MetaColumn> getMetaColumn(Column column) {
        return Optional.ofNullable(quickAccessMetaColumn.get(column.getEntityName() + "." + column.getColumnName()));
    }

    public Optional<MetaTable> getMetaTable(Entity entity) {
        return Optional.ofNullable(quickAccessMetaTable.get(entity.getEntityName()));
    }

}
