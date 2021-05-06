package io.daobab.target.meta.table;


import io.daobab.clone.EntityDuplicator;
import io.daobab.model.Table;
import io.daobab.model.TableColumn;
import io.daobab.target.meta.column.*;

import java.util.Arrays;
import java.util.List;

public class MetaPrimaryKey extends Table implements
        MetaCatalogName<MetaPrimaryKey>,
        MetaSchemaName<MetaPrimaryKey>,
        MetaTableName<MetaPrimaryKey>,
        CamelName<MetaPrimaryKey>,
        MetaColumnName<MetaPrimaryKey>,
        TableColumnName<MetaPrimaryKey>,
        PkName<MetaPrimaryKey>,
        KeySeq<MetaPrimaryKey> {

    @Override
    public String getEntityName() {
        return "PRIMARY_KEY";
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

    @Override
    public MetaPrimaryKey clone() {
        return EntityDuplicator.cloneEntity(this);
    }


}