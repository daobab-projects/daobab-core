package io.daobab.target.buffer.noheap.access;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;

public class BitFieldBlob extends BitField<byte[]> {

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, byte[] bytearray) {
        if (bytearray != null) {
            byteBuffer.put(position, (byte) 1);
            byteBuffer.putInt(position + CHECK_NULL_SIZE, bytearray.length);
            byteBuffer.position(position + CHECK_NULL_SIZE + INT_SIZE);
            byteBuffer.put(bytearray);
            return;
        }
        byteBuffer.put(position, (byte) 0);
    }

    @Override
    public byte[] readValue(ByteBuffer byteBuffer, Integer position) {
        if (byteBuffer.get(position) == 0) {
            return new byte[0];
        }
        int length = byteBuffer.getInt(position + CHECK_NULL_SIZE);
        byte[] bytearray = new byte[length];
        byteBuffer.position(position + CHECK_NULL_SIZE + INT_SIZE);
        byteBuffer.get(bytearray);
        return bytearray;
    }

    @Override
    public Class<byte[]> getClazz() {
        return byte[].class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return INT_SIZE + CHECK_NULL_SIZE;
    }

}
