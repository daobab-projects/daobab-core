package io.daobab.target.buffer.noheap.access.field;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;

public class BitFieldFloatNotNull implements BitField<Float> {

    public BitFieldFloatNotNull(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Float val) {
        byteBuffer.putFloat(position, val);
    }

    @Override
    public Float readValue(ByteBuffer byteBuffer, Integer position) {
        return byteBuffer.getFloat(position);
    }

    @Override
    public Class<Float> getClazz() {
        return Float.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.FLOAT;
    }


}
