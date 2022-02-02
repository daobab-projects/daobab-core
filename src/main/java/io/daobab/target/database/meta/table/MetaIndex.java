package io.daobab.target.database.meta.table;


import io.daobab.clone.EntityDuplicator;
import io.daobab.model.Table;
import io.daobab.model.TableColumn;
import io.daobab.target.database.meta.column.*;

import java.util.Arrays;
import java.util.List;

public class MetaIndex extends Table implements
        MetaCatalogName<MetaIndex>,
        MetaSchemaName<MetaIndex>,
        MetaTableName<MetaIndex>,
        Unique<MetaIndex>,
        MetaIndexName<MetaIndex>,
        MetaIndexType<MetaIndex>,
        OrdinalPosition<MetaIndex>,
        MetaColumnName<MetaIndex>,
        Ascending<MetaIndex>,
        Cardinality<MetaIndex>,
        TableColumnName<MetaIndex>,
        FilterCondition<MetaIndex>

         {

    @Override
    public String getEntityName() {
        return "META_INDEX";
    }

    @Override
    public List<TableColumn> columns() {
        return Arrays.asList(
                new TableColumn(colCatalogName()).size(256),
                new TableColumn(colSchemaName()).size(256),
                new TableColumn(colTableName()).size(256),
                new TableColumn(colUnique()),
                new TableColumn(colIndexName()).size(256),
                new TableColumn(colIndexType()),
                new TableColumn(colOrdinalPosition()),
                new TableColumn(colColumnName()).size(256),
                new TableColumn(colAscending()),
                new TableColumn(colCardinality()),
                new TableColumn(colTableColumnName()).size(10),
                new TableColumn(colFilterCondition()).size(1024)
        );
    }

    @Override
    public MetaIndex clone() {
        return EntityDuplicator.cloneEntity(this);
    }


}
