package io.daobab.target.buffer.noheap.access;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;

public class BitFieldLong extends BitField<Long> {

    public static int INTEGER_BIT_SIZE = CHECK_NULL_SIZE + LONG_SIZE;

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Long val) {
        if (val != null) {
            byteBuffer.put(position, (byte) 1);
            byteBuffer.putLong(position + 1, val);
            return;
        }
        byteBuffer.put(position, (byte) 0);
    }

    @Override
    public Long readValue(ByteBuffer byteBuffer, Integer position) {
        if (byteBuffer.get(position) == 0) {
            return null;
        }
        return byteBuffer.getLong(position + 1);
    }

    @Override
    public Class<Long> getClazz() {
        return Long.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return LONG_SIZE + CHECK_NULL_SIZE;
    }
}
