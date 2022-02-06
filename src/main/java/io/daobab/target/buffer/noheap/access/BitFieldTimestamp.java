package io.daobab.target.buffer.noheap.access;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.time.Instant;

public class BitFieldTimestamp extends BitField<Timestamp> {

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Object val) {
        if (val != null) {
            byteBuffer.put(position, (byte) 1);
            Timestamp tval = (Timestamp) val;
            Instant instant = tval.toInstant();
            byteBuffer.putLong(position + CHECK_NULL_SIZE, instant.getEpochSecond());
            byteBuffer.putInt(position + CHECK_NULL_SIZE + LONG_SIZE, instant.getNano());
            return;
        }
        byteBuffer.put(position, (byte) 0);
    }

    @Override
    public Timestamp readValue(ByteBuffer byteBuffer, Integer position) {
        if (byteBuffer.get(position) == 0) {
            return null;
        }
        Instant instant = Instant.ofEpochSecond((byteBuffer.getLong(position + CHECK_NULL_SIZE)));
        return Timestamp.from(instant.plusNanos(byteBuffer.getInt(position + CHECK_NULL_SIZE + LONG_SIZE)));
    }

    @Override
    public Class<Timestamp> getClazz() {
        return Timestamp.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return LONG_SIZE + INT_SIZE + CHECK_NULL_SIZE;
    }

}
