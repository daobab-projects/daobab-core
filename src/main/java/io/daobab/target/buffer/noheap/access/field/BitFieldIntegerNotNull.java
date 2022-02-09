package io.daobab.target.buffer.noheap.access.field;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;

public class BitFieldIntegerNotNull implements BitField<Integer> {

    public BitFieldIntegerNotNull() {
        this(new EmptyTableColumn());
    }

    public BitFieldIntegerNotNull(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Integer val) {
        byteBuffer.putInt(position, val);
    }

    @Override
    public Integer readValue(ByteBuffer byteBuffer, Integer position) {
        return byteBuffer.getInt(position);
    }

    @Override
    public Class<Integer> getClazz() {
        return Integer.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.INT;
    }


}
