package io.daobab.target.meta;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.target.QueryTarget;
import io.daobab.target.multi.MultiEntity;
import io.daobab.target.database.JdbcType;
import io.daobab.target.meta.column.Datatype;
import io.daobab.target.meta.column.MetaColumnSize;
import io.daobab.target.meta.table.MetaColumn;
import io.daobab.target.meta.table.MetaTable;

import java.util.Optional;

public interface MetaData extends MultiEntity, QueryTarget {
    Optional<MetaColumn> getMetaColumn(Column column);

    Optional<MetaTable> getMetaTable(Entity entity);

    default JdbcType getColumnDataType(Column column) {
        return getMetaColumn(column).map(Datatype::getDatatype).orElse(null);
    }

    default int getColumnSize(Column column) {
        return getMetaColumn(column).map(MetaColumnSize::getColumnSize).orElse(0);
    }

    String getCatalog();
    String getSchema();
    int getTablesCount();
    int getColumnsCount();

}
