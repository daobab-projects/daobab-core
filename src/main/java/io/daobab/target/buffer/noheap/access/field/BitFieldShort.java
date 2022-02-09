package io.daobab.target.buffer.noheap.access.field;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;

public class BitFieldShort implements BitField<Short> {

    public BitFieldShort(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Short val) {
        if (val != null) {
            byteBuffer.put(position, (byte) 1);
            byteBuffer.putShort(position + 1, val);
            return;
        }
        byteBuffer.put(position, (byte) 0);
    }

    @Override
    public Short readValue(ByteBuffer byteBuffer, Integer position) {
        if (byteBuffer.get(position) == 0) {
            return null;
        }
        return byteBuffer.getShort(position + 1);
    }

    @Override
    public Class<Short> getClazz() {
        return Short.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.SHORT + BitSize.CHECK_NULL;
    }

}
