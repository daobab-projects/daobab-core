package io.daobab.target.meta;

import io.daobab.target.meta.table.*;

public interface MetaDataTables {
    MetaSchema tabMetaSchema = new MetaSchema();
    MetaTable tabMetaTable = new MetaTable();
    MetaColumn tabMetaColumn = new MetaColumn();
    MetaPrimaryKey tabPrimaryKey = new MetaPrimaryKey();
    MetaForeignKey tabForeignKey = new MetaForeignKey();
    MetaIndex tabMetaIndex = new MetaIndex();
}
