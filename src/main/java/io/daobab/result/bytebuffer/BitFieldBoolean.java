package io.daobab.result.bytebuffer;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;

public class BitFieldBoolean extends BitField<Boolean> {

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Object val) {
        if (val != null) {
            Boolean v = (Boolean) val;
            byteBuffer.put(position, (byte) (Boolean.TRUE.equals(v) ? 2 : 1));
            byteBuffer.put(position + 1, (byte) val);
            return;
        }
        byteBuffer.put(position, (byte) 0); //2 means null in Boolean
    }

    @Override
    public Boolean readValue(ByteBuffer byteBuffer, Integer position) {
        if (byteBuffer.get(position) == 0) { //0 means null
            return null;
        }
        return byteBuffer.get(position + 1) == 2;
    }

    @Override
    public Class<Boolean> getClazz() {
        return Boolean.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return 1 + CHECK_NULL_SIZE;
    }

}
