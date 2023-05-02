package io.daobab.target.buffer.nonheap.access.field;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;
import java.util.Comparator;

public class BitFieldByte extends BitFieldComparable<Byte> {

    public BitFieldByte(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Byte val) {
        byteBuffer.put(position, val);
    }

    @Override
    public Byte readValue(ByteBuffer byteBuffer, Integer position) {
        return byteBuffer.get();
    }

    @Override
    public Class<Byte> getClazz() {
        return Byte.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return 1;
    }

    @Override
    public Comparator<? super Byte> comparator() {
        return null;
    }

}
