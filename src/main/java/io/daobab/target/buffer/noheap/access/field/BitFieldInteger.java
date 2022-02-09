package io.daobab.target.buffer.noheap.access.field;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;

public class BitFieldInteger implements BitField<Integer> {

    public BitFieldInteger() {
    }

    public BitFieldInteger(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Integer val) {

        if (val != null) {
            byteBuffer.put(position, (byte) 1);
            byteBuffer.putInt(position + BitSize.CHECK_NULL, val);
            return;
        }
        byteBuffer.put(position, (byte) 0);

    }

    @Override
    public Integer readValue(ByteBuffer byteBuffer, Integer position) {
        if (byteBuffer.get(position) == 0) {
            return null;
        }
        return byteBuffer.getInt(position + BitSize.CHECK_NULL);
    }

    @Override
    public Class<Integer> getClazz() {
        return Integer.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.INT + BitSize.CHECK_NULL;
    }


}
