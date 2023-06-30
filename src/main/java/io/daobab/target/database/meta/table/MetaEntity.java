package io.daobab.target.database.meta.table;

import io.daobab.error.DaobabException;
import io.daobab.model.Column;
import io.daobab.model.RelatedTo;
import io.daobab.model.Table;
import io.daobab.model.TableColumn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class MetaEntity extends Table<MetaEntity> {

    private String tableName;

    public MetaEntity() {
        super();
    }

    public MetaEntity(Map<String, Object> parameters) {
        super(parameters);
    }
    List<TableColumn> columnList = new ArrayList<>();

    public MetaEntity(String tableName) {
        this();
        this.tableName = tableName;
    }

    public MetaEntity(String tableName, List<MetaColumn> columns) {
        this.tableName = tableName;
        columns.forEach(c -> columnList.add(toColumn(this, c)));
    }

    public void addColumn(TableColumn tc) {
        columnList.add(tc);
    }

    public TableColumn addColumn(MetaColumn mc) {
        TableColumn tc = toColumn(this, mc);
        return tc;
    }

    @Override
    public List<TableColumn> columns() {
        return columnList;
    }

    @Override
    public String getEntityName() {
        return tableName;
    }

    public Column<?, ?, ?> getColumnByName(String name) {
        for (TableColumn tableColumn : columns()) {
            if (tableColumn.getColumn().getColumnName().equals(name)) {
                return tableColumn.getColumn();
            }
        }
        return null;
    }


    public TableColumn toColumn(MetaEntity entity, MetaColumn c) {
        return new TableColumn(new Column<MetaEntity, Object, RelatedTo>() {
            @Override
            public String getColumnName() {
                return c.getColumnName();
            }

            @Override
            public String getFieldName() {
                return c.getColumnName();
            }

            @Override
            public Class<Object> getFieldClass() {
                Class<?> c1 = c.getFieldClass();
                return (Class<Object>) c1;
            }

            @Override
            public Object getValue(RelatedTo entity) {
                throw new DaobabException("Operation cannot be done");
            }

            @Override
            public RelatedTo setValue(RelatedTo entity, Object value) {
                throw new DaobabException("Operation cannot be done");
            }

            @Override
            public MetaEntity getInstance() {
                return entity;
            }
        });

    }

}
