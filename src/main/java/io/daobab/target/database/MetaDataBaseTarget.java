package io.daobab.target.database;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.result.EntityList;
import io.daobab.target.multi.AboveMultiEntityTarget;
import io.daobab.target.meta.MetaData;
import io.daobab.target.meta.table.MetaColumn;
import io.daobab.target.meta.table.MetaTable;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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

            ResultSet rsPk = databaseMetaData.getPrimaryKeys("", schema, mt.getTableName());
            while (rsPk.next()) {
                primaryKeyColumn = rsPk.getString("COLUMN_NAME");
            }

            ResultSet rsColumn = databaseMetaData.getColumns("", schema, mt.getTableName(), "%");

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

//        put(tables,columns);

        tables.forEach(t -> quickAccessMetaTable.put(t.getEntityName(), t));
        columns.forEach(t -> quickAccessMetaColumn.put(t.getTableColumnName(), t));
        put(new EntityList<>(tables, MetaTable.class), new EntityList<>(columns, MetaColumn.class));

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
