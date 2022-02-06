package io.daobab.target.buffer.noheap.access;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;

public class BitFieldLong extends BitField<Long> {

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Object val) {
        if (val != null) {
            byteBuffer.put(position, (byte) 1);
            byteBuffer.putLong(position + 1, (long) val);
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
