package io.daobab.target.buffer.noheap.access.field;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;

public class BitFieldByte implements BitField<Byte> {

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

}
