package io.daobab.target.database.meta.table;


import io.daobab.clone.EntityDuplicator;
import io.daobab.model.Column;
import io.daobab.model.PrimaryKey;
import io.daobab.model.Table;
import io.daobab.model.TableColumn;
import io.daobab.target.database.meta.column.MetaCatalogName;
import io.daobab.target.database.meta.column.MetaSchemaName;
import io.daobab.target.database.meta.column.Remarks;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("rawtypes")
public class MetaSchema extends Table<MetaSchema> implements
        Remarks<MetaSchema>,
        MetaCatalogName<MetaSchema>,
        MetaSchemaName<MetaSchema>,
        PrimaryKey<MetaSchema, String, MetaSchemaName> {

    public MetaSchema() {
        super();
    }

    public MetaSchema(Map<String, Object> parameters) {
        super(parameters);
    }


    @Override
    public String getEntityName() {
        return "META_SCHEMA";
    }

    @Override
    public List<TableColumn> columns() {
        return Arrays.asList(
                new TableColumn(colRemarks()).size(30),
                new TableColumn(colCatalogName()).size(256),
                new TableColumn(colSchemaName()).size(256)
        );
    }


    @Override
    public MetaSchema clone() {
        return EntityDuplicator.cloneEntity(this);
    }

    @Override
    public Column<MetaSchema, String, MetaSchemaName> colID() {
        return colSchemaName();
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        PrimaryKey<?, ?, ?> other = (PrimaryKey<?, ?, ?>) obj;
        return Objects.equals(getId(), other.getId());
    }

}
