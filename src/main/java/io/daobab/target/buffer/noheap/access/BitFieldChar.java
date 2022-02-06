package io.daobab.target.buffer.noheap.access;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;

public class BitFieldChar extends BitField<Character> {

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Object val) {
        if (val != null) {
            byteBuffer.put(position, (byte) 1);
            byteBuffer.putChar(position + 1, (char) val);
            return;
        }
        byteBuffer.put(position, (byte) 0);
    }

    @Override
    public Character readValue(ByteBuffer byteBuffer, Integer position) {
        if (byteBuffer.get(position) == 0) {
            return null;
        }
        return byteBuffer.getChar(position + 1);
    }

    @Override
    public Class<Character> getClazz() {
        return Character.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return 2 + CHECK_NULL_SIZE;
    }

}
