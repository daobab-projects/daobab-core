package io.daobab.result.bytebuffer;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;

public class BitFieldDouble extends BitField<Double> {

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Object aDouble) {
        if (aDouble != null) {
            byteBuffer.put(position, (byte) 1);
            byteBuffer.putDouble(position + 1, (double) aDouble);
            return;
        }
        byteBuffer.put(position, (byte) 0);
    }

    @Override
    public Double readValue(ByteBuffer byteBuffer, Integer position) {
        if (byteBuffer.get(position) == 0) {
            return null;
        }
        return byteBuffer.getDouble(position + 1);
    }

    @Override
    public Class<Double> getClazz() {
        return Double.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return 8 + CHECK_NULL_SIZE;
    }

}
