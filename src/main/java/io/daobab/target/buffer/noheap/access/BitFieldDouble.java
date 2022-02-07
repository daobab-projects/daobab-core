package io.daobab.target.buffer.noheap.access;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;

public class BitFieldDouble extends BitField<Double> {

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Double aDouble) {
        if (aDouble != null) {
            byteBuffer.put(position, (byte) 1);
            byteBuffer.putDouble(position + 1, aDouble);
            return;
        }
        byteBuffer.put(position, (byte) 0);
    }

    @Override
    public Double readValue(ByteBuffer byteBuffer, Integer position) {
        if (byteBuffer.get(position) == 0) {
            return null;
        }
        return byteBuffer.getDouble(position + 1);
    }

    @Override
    public Class<Double> getClazz() {
        return Double.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return DOUBLE_SIZE + CHECK_NULL_SIZE;
    }

}
