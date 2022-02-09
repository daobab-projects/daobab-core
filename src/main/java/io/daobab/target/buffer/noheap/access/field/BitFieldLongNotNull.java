package io.daobab.target.buffer.noheap.access.field;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;

public class BitFieldLongNotNull implements BitField<Long> {

    public BitFieldLongNotNull(TableColumn tableColumn) {
    }


    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Long val) {
        byteBuffer.putLong(position, val);
    }

    @Override
    public Long readValue(ByteBuffer byteBuffer, Integer position) {
        return byteBuffer.getLong(position);
    }

    @Override
    public Class<Long> getClazz() {
        return Long.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.LONG;
    }
}
