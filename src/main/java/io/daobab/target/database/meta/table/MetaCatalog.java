package io.daobab.target.database.meta.table;


import io.daobab.model.*;
import io.daobab.target.database.meta.column.MetaCatalogName;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
@TableInformation(name = "META_CATALOG")
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
    public List<TableColumn> columns() {
        return Arrays.asList(
                new TableColumn(colCatalogName()).size(256)
        );
    }


    @Override
    public Column<MetaCatalog, String, MetaCatalogName> colID() {
        return colCatalogName();
    }



}
