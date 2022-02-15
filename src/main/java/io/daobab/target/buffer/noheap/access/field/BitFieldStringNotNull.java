package io.daobab.target.buffer.noheap.access.field;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class BitFieldStringNotNull implements BitField<String> {

    int length;

    public BitFieldStringNotNull(TableColumn tableColumn) {
        this.length = tableColumn.getSize();
    }

    public static int calculateSpaceForLength(int length) {
        return (length * 6 + BitSize.INT);
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, String val) {
        byteBuffer.putInt(position, val.length());
        byteBuffer.position(position + BitSize.INT);
        byteBuffer.put(val.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String readValue(ByteBuffer byteBuffer, Integer position) {
        byteBuffer.position(position);
        byte[] read = new byte[byteBuffer.getInt()];
        byteBuffer.position(position + BitSize.INT);
        byteBuffer.get(read);
        return new String(read, StandardCharsets.UTF_8);
    }

    @Override
    public Class<String> getClazz() {
        return String.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return (length * 6) + BitSize.INT;
    }

}
