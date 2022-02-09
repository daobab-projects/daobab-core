package io.daobab.target.buffer.noheap.access.field;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;

public class BitFieldFloat implements BitField<Float> {

    public BitFieldFloat(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Float val) {
        if (val != null) {
            byteBuffer.put(position, (byte) 1);
            byteBuffer.putFloat(position + 1, val);
            return;
        }
        byteBuffer.put(position, (byte) 0);
    }

    @Override
    public Float readValue(ByteBuffer byteBuffer, Integer position) {
        if (byteBuffer.get(position) == 0) {
            return null;
        }
        return byteBuffer.getFloat(position + 1);
    }

    @Override
    public Class<Float> getClazz() {
        return Float.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.FLOAT + BitSize.CHECK_NULL;
    }

}
