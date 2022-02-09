package io.daobab.target.buffer.noheap.access.field;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.time.Instant;

public class BitFieldTimestamp implements BitField<Timestamp> {

    public BitFieldTimestamp(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Timestamp val) {
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
    public Timestamp readValue(ByteBuffer byteBuffer, Integer position) {
        if (byteBuffer.get(position) == 0) {
            return null;
        }
        Instant instant = Instant.ofEpochSecond((byteBuffer.getLong(position + BitSize.CHECK_NULL)));
        return Timestamp.from(instant.plusNanos(byteBuffer.getInt(position + BitSize.CHECK_NULL + BitSize.LONG)));
    }

    @Override
    public Class<Timestamp> getClazz() {
        return Timestamp.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.LONG + BitSize.INT + BitSize.CHECK_NULL;
    }

}
