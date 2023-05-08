package io.daobab.target.database.meta;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.target.buffer.BufferQueryTarget;
import io.daobab.target.buffer.multi.MultiEntity;
import io.daobab.target.buffer.single.Entities;
import io.daobab.target.database.connection.JdbcType;
import io.daobab.target.database.meta.column.Datatype;
import io.daobab.target.database.meta.column.MetaColumnSize;
import io.daobab.target.database.meta.table.*;

import java.util.Optional;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings({"rawtypes", "unused"})
public interface MetaData extends MultiEntity, BufferQueryTarget {

    MetaSchema tabMetaSchema = new MetaSchema();
    MetaTable tabMetaTable = new MetaTable();
    MetaColumn tabMetaColumn = new MetaColumn();
    MetaPrimaryKey tabMetaPrimaryKey = new MetaPrimaryKey();
    MetaForeignKey tabMetaForeignKey = new MetaForeignKey();
    MetaIndex tabMetaIndex = new MetaIndex();


    Entities<MetaTable> getMetaTables();

    Entities<MetaColumn> getMetaColumns();

    Entities<MetaSchema> getMetaSchemas();

    MetaEntity getMetaEntityFor(MetaTable metaTable);

    Optional<MetaColumn> getMetaColumn(Column column);

    Optional<MetaTable> getMetaTable(Entity entity);

    default JdbcType getColumnDataType(Column column) {
        return getMetaColumn(column).map(Datatype::getDatatype).orElse(null);
    }

    default int getColumnSize(Column column) {
        return getMetaColumn(column).map(MetaColumnSize::getColumnSize).orElse(0);
    }
}
