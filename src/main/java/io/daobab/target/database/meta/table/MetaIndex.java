package io.daobab.target.database.meta.table;


import io.daobab.model.Table;
import io.daobab.model.TableColumn;
import io.daobab.model.TableName;
import io.daobab.target.database.meta.column.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@TableName("META_INDEX")
public class MetaIndex extends Table<MetaIndex> implements
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
        FilterCondition<MetaIndex> {

    public MetaIndex() {
        super();
    }

    public MetaIndex(Map<String, Object> parameters) {
        super(parameters);
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

}
