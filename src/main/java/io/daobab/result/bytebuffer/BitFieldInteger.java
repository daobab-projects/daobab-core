package io.daobab.result.bytebuffer;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;

public class BitFieldInteger extends BitField<Integer> {

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Object val) {

        if (val != null) {
            byteBuffer.put(position, (byte) 1);
            byteBuffer.putInt(position + CHECK_NULL_SIZE, (int) val);
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
