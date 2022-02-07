package io.daobab.target.buffer.noheap.access;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;

public class BitFieldInteger extends BitField<Integer> {

    public static int INTEGER_BIT_SIZE = CHECK_NULL_SIZE + INT_SIZE;

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Integer val) {

        if (val != null) {
            byteBuffer.put(position, (byte) 1);
            byteBuffer.putInt(position + CHECK_NULL_SIZE, val);
            return;
        }
        byteBuffer.put(position, (byte) 0);

    }

    @Override
    public Integer readValue(ByteBuffer byteBuffer, Integer position) {
        if (byteBuffer.get(position) == 0) {
            return null;
        }
        return byteBuffer.getInt(position + CHECK_NULL_SIZE);
    }

    @Override
    public Class<Integer> getClazz() {
        return Integer.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return INT_SIZE + CHECK_NULL_SIZE;
    }


}
