package io.daobab.target.buffer.noheap.access.field;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.Date;

public class BitFieldDate implements BitField<Date> {

    public BitFieldDate(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Date val) {
        if (val != null) {
            byteBuffer.put(position, (byte) 1);
            Instant instant = val.toInstant();

            byteBuffer.putLong(position + BitSize.CHECK_NULL, instant.getEpochSecond());
            byteBuffer.putInt(position + BitSize.CHECK_NULL + BitSize.LONG, instant.getNano());
            return;
        }
        byteBuffer.put(position, (byte) 0);
    }

    @Override
    public Date readValue(ByteBuffer byteBuffer, Integer position) {
        if (byteBuffer.get(position) == 0) {
            return null;
        }
        Instant instant = Instant.ofEpochSecond((byteBuffer.getLong(position + BitSize.CHECK_NULL)));
        return Date.from(instant.plusNanos(byteBuffer.getInt(position + BitSize.CHECK_NULL + BitSize.LONG)));
    }

    @Override
    public Class<Date> getClazz() {
        return Date.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.CHECK_NULL + BitSize.DATE_UTIL;
    }

}
