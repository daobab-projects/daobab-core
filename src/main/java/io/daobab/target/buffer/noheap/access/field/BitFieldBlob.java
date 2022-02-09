package io.daobab.target.buffer.noheap.access.field;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;

public class BitFieldBlob implements BitField<byte[]> {

    public BitFieldBlob(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, byte[] bytearray) {
        if (bytearray != null) {
            byteBuffer.put(position, (byte) 1);
            byteBuffer.putInt(position + BitSize.CHECK_NULL, bytearray.length);
            byteBuffer.position(position + BitSize.CHECK_NULL + BitSize.INT);
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
        int length = byteBuffer.getInt(position + BitSize.CHECK_NULL);
        byte[] bytearray = new byte[length];
        byteBuffer.position(position + BitSize.CHECK_NULL + BitSize.INT);
        byteBuffer.get(bytearray);
        return bytearray;
    }

    @Override
    public Class<byte[]> getClazz() {
        return byte[].class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.INT + BitSize.CHECK_NULL;
    }

}
