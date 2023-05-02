package io.daobab.target.buffer.nonheap.access.field;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;
import java.util.Comparator;

public class BitFieldChar extends BitFieldComparable<Character> {

    public BitFieldChar(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Character val) {
        if (val != null) {
            byteBuffer.put(position, (byte) 1);
            byteBuffer.putChar(position + 1, val);
            return;
        }
        byteBuffer.put(position, (byte) 0);
    }

    @Override
    public Character readValue(ByteBuffer byteBuffer, Integer position) {
        if (byteBuffer.get(position) == 0) {
            return null;
        }
        return byteBuffer.getChar(position + BitSize.NULL);
    }

    @Override
    public Class<Character> getClazz() {
        return Character.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return 2 + BitSize.NULL;
    }

    @Override
    public Comparator<? super Character> comparator() {
        return null;
    }

}
