package io.daobab.target.database.meta.table;


import io.daobab.clone.EntityDuplicator;
import io.daobab.model.Table;
import io.daobab.model.TableColumn;
import io.daobab.target.database.meta.column.*;

import java.util.Arrays;
import java.util.List;

public class MetaForeignKey extends Table implements
        PkTableCatalogName<MetaForeignKey>,
        PkTableSchemaName<MetaForeignKey>,
        PkTableName<MetaForeignKey>,
        PkColumnName<MetaForeignKey>,
        FkTableCatalogName<MetaForeignKey>,
        FkTableSchemaName<MetaForeignKey>,
        FkTableName<MetaForeignKey>,
        FkColumnName<MetaForeignKey>,
        KeySeq<MetaForeignKey>,
        UpdateRule<MetaForeignKey>,
        DeleteRule<MetaForeignKey>,
        FkName<MetaForeignKey>,
        PkName<MetaForeignKey> {

    @Override
    public String getEntityName() {
        return "FOREIGN_KEY";
    }

    @Override
    public List<TableColumn> columns() {
        return Arrays.asList(
                new TableColumn(colPkCatalogName()).size(256),
                new TableColumn(colPkSchemaName()).size(256),
                new TableColumn(colPkTableName()).size(256),
                new TableColumn(colPkColumnName()).size(256),
                new TableColumn(colPkName()).size(256),
                new TableColumn(colFkCatalogName()).size(256),
                new TableColumn(colFkSchemaName()).size(256),
                new TableColumn(colFkTableName()).size(256),
                new TableColumn(colFkColumnName()).size(256),
                new TableColumn(colFkName()).size(256),
                new TableColumn(colKeySeq()),
                new TableColumn(colUpdateRule()),
                new TableColumn(colDeleteRule())
        );
    }

    @Override
    public MetaForeignKey clone() {
        return EntityDuplicator.cloneEntity(this);
    }


}
