package io.daobab.target.buffer.noheap.access;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;

public class BitFieldByte extends BitField<Byte> {
    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Object val) {
        byteBuffer.put(position, (byte) val);
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
