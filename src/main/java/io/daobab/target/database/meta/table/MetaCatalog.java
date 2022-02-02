package io.daobab.target.database.meta.table;


import io.daobab.clone.EntityDuplicator;
import io.daobab.model.Column;
import io.daobab.model.PrimaryKey;
import io.daobab.model.Table;
import io.daobab.model.TableColumn;
import io.daobab.target.database.meta.column.MetaCatalogName;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MetaCatalog extends Table implements
        MetaCatalogName<MetaCatalog>,

        PrimaryKey<MetaCatalog, String, MetaCatalogName> {


    @Override
    public String getEntityName() {
        return "META_SCHEMA";
    }

    @Override
    public List<TableColumn> columns() {
        return Arrays.asList(
                new TableColumn(colCatalogName()).size(256)
        );
    }


    @Override
    public MetaCatalog clone() {
        return EntityDuplicator.cloneEntity(this);
    }

    @Override
    public Column<MetaCatalog, String, MetaCatalogName> colID() {
        return colCatalogName();
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        PrimaryKey other = (PrimaryKey) obj;
        return Objects.equals(getId(), other.getId());
    }

}
