package io.daobab.target.meta;

import io.daobab.target.meta.table.MetaColumn;
import io.daobab.target.meta.table.MetaPrimaryKey;
import io.daobab.target.meta.table.MetaSchema;
import io.daobab.target.meta.table.MetaTable;

public interface MetaSpecificsTables {
    MetaSchema tabMetaSchema = new MetaSchema();
    MetaTable tabMetaTable = new MetaTable();
    MetaColumn tabMetaColumn = new MetaColumn();
    MetaPrimaryKey tabPrimaryKey = new MetaPrimaryKey();
}
