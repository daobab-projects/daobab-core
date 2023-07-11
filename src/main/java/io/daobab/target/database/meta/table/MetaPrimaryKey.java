package io.daobab.target.database.meta.table;


import io.daobab.model.Table;
import io.daobab.model.TableColumn;
import io.daobab.model.TableInformation;
import io.daobab.target.database.meta.column.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@TableInformation(name = "PRIMARY_KEY")
public class MetaPrimaryKey extends Table<MetaPrimaryKey> implements
        MetaCatalogName<MetaPrimaryKey>,
        MetaSchemaName<MetaPrimaryKey>,
        MetaTableName<MetaPrimaryKey>,
        CamelName<MetaPrimaryKey>,
        MetaColumnName<MetaPrimaryKey>,
        TableColumnName<MetaPrimaryKey>,
        PkName<MetaPrimaryKey>,
        KeySeq<MetaPrimaryKey> {

    public MetaPrimaryKey() {
        super();
    }

    public MetaPrimaryKey(Map<String, Object> parameters) {
        super(parameters);
    }

    @Override
    public List<TableColumn> columns() {
        return Arrays.asList(
                new TableColumn(colColumnName()).size(256),
                new TableColumn(colKeySeq()).size(256),
                new TableColumn(colPkName()).size(10),
                new TableColumn(colTableName()).size(256),
                new TableColumn(colCamelName()).size(256),
                new TableColumn(colSchemaName()).size(256),
                new TableColumn(colCatalogName()).size(256)
        );
    }


}
