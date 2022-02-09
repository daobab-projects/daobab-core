package io.daobab.target.buffer.noheap.access.field;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;

public class BitFieldDoubleNotNull implements BitField<Double> {

    public BitFieldDoubleNotNull(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Double val) {
        byteBuffer.putDouble(position, val);
    }

    @Override
    public Double readValue(ByteBuffer byteBuffer, Integer position) {
        return byteBuffer.getDouble(position);
    }

    @Override
    public Class<Double> getClazz() {
        return Double.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.DOUBLE;
    }

}
