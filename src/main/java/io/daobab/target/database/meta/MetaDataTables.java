package io.daobab.target.database.meta;

import io.daobab.target.database.meta.table.*;

public interface MetaDataTables {
    MetaSchema tabMetaSchema = new MetaSchema();
    MetaTable tabMetaTable = new MetaTable();
    MetaColumn tabMetaColumn = new MetaColumn();
    MetaPrimaryKey tabMetaPrimaryKey = new MetaPrimaryKey();
    MetaForeignKey tabMetaForeignKey = new MetaForeignKey();
    MetaIndex tabMetaIndex = new MetaIndex();
}
