package io.daobab.target.buffer.noheap.access.field;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.time.Instant;

public class BitFieldTimestampNotNull implements BitField<Timestamp> {

    public BitFieldTimestampNotNull(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Timestamp val) {
        Instant instant = val.toInstant();
        byteBuffer.putLong(position, instant.getEpochSecond());
        byteBuffer.putInt(position + BitSize.LONG, instant.getNano());
    }

    @Override
    public Timestamp readValue(ByteBuffer byteBuffer, Integer position) {
        Instant instant = Instant.ofEpochSecond((byteBuffer.getLong(position)));
        return Timestamp.from(instant.plusNanos(byteBuffer.getInt(position + BitSize.LONG)));
    }

    @Override
    public Class<Timestamp> getClazz() {
        return Timestamp.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.TIMESTAMP_UTIL;
    }

}
