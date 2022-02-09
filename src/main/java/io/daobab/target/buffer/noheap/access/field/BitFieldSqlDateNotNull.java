package io.daobab.target.buffer.noheap.access.field;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;
import java.sql.Date;
import java.time.Instant;

public class BitFieldSqlDateNotNull implements BitField<Date> {

    public BitFieldSqlDateNotNull(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Date val) {
        Instant instant = val.toInstant();
        byteBuffer.putLong(position, instant.getEpochSecond());
        byteBuffer.putInt(position + BitSize.LONG, instant.getNano());
    }

    @Override
    public Date readValue(ByteBuffer byteBuffer, Integer position) {
        Instant instant = Instant.ofEpochSecond((byteBuffer.getLong(position)));
        java.util.Date utilDate = java.util.Date.from(instant.plusNanos(byteBuffer.getInt(position + BitSize.LONG)));
        return new Date(utilDate.getTime());
    }

    @Override
    public Class<Date> getClazz() {
        return Date.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.DATE_SQL;
    }

}
