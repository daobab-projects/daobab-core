package io.daobab.target.database.meta.table;


import io.daobab.model.*;
import io.daobab.target.database.meta.column.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@TableName(value = "META_TABLE")
public class MetaTable extends Table<MetaTable> implements
        Remarks<MetaTable>,
        MetaTableName<MetaTable>,
        MetaSchemaName<MetaTable>,
        CamelName<MetaTable>,
        MetaCatalogName<MetaTable>,
        ColumnCount<MetaTable>,
        TableType<MetaTable>,

        PrimaryKey<MetaTable, String, MetaTableName> {

    public MetaTable() {
        super();
    }

    public MetaTable(Map<String, Object> parameters) {
        super(parameters);
    }


    @Override
    public List<TableColumn> columns() {
        return Arrays.asList(
                new TableColumn(colRemarks()).size(256),
                new TableColumn(colTableName()).size(256),
                new TableColumn(colColumnCount()),
                new TableColumn(colSchemaName()).size(256),
                new TableColumn(colCamelName()).size(256),
                new TableColumn(colCatalogName()).size(256),
                new TableColumn(colTableType()).size(256)
        );
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Column<MetaTable, String, MetaTableName> colID() {
        return colTableName();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        PrimaryKey other = (PrimaryKey) obj;
        return Objects.equals(getId(), other.getId());
    }

}
