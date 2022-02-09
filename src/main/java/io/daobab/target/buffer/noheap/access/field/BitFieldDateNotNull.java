package io.daobab.target.buffer.noheap.access.field;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.Date;

public class BitFieldDateNotNull implements BitField<Date> {

    public BitFieldDateNotNull(TableColumn tableColumn) {
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
        return Date.from(instant.plusNanos(byteBuffer.getInt(position + BitSize.LONG)));
    }

    @Override
    public Class<Date> getClazz() {
        return Date.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.DATE_UTIL;
    }

}
