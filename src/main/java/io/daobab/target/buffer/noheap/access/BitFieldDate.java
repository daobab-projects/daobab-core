package io.daobab.target.buffer.noheap.access;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.Date;

public class BitFieldDate extends BitField<Date> {

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Date val) {
        if (val != null) {
            byteBuffer.put(position, (byte) 1);
            Instant instant = val.toInstant();

            byteBuffer.putLong(position + CHECK_NULL_SIZE, instant.getEpochSecond());
            byteBuffer.putInt(position + CHECK_NULL_SIZE + LONG_SIZE, instant.getNano());
            return;
        }
        byteBuffer.put(position, (byte) 0);
    }

    @Override
    public Date readValue(ByteBuffer byteBuffer, Integer position) {
        if (byteBuffer.get(position) == 0) {
            return null;
        }
        Instant instant = Instant.ofEpochSecond((byteBuffer.getLong(position + 1)));
        return Date.from(instant.plusNanos(byteBuffer.getInt(position + CHECK_NULL_SIZE + LONG_SIZE)));
    }

    @Override
    public Class<Date> getClazz() {
        return Date.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return LONG_SIZE + INT_SIZE + CHECK_NULL_SIZE;
    }

}
