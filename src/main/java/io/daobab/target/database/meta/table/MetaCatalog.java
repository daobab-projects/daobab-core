package io.daobab.target.database.meta.table;


import io.daobab.model.Column;
import io.daobab.model.PrimaryKey;
import io.daobab.model.Table;
import io.daobab.model.TableColumn;
import io.daobab.target.database.meta.column.MetaCatalogName;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("rawtypes")
public class MetaCatalog extends Table<MetaCatalog> implements
        MetaCatalogName<MetaCatalog>,

        PrimaryKey<MetaCatalog, String, MetaCatalogName> {

    public MetaCatalog() {
        super();
    }

    public MetaCatalog(Map<String, Object> parameters) {
        super(parameters);
    }


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
