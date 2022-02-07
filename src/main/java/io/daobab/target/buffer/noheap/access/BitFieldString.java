package io.daobab.target.buffer.noheap.access;

import io.daobab.error.DaobabException;
import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class BitFieldString extends BitField<String> {

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, String val) {

        if (val != null) {
            byteBuffer.put(position, (byte) 1);

            byteBuffer.putInt(position + CHECK_NULL_SIZE, val.length());
            byteBuffer.position(position + INT_SIZE + CHECK_NULL_SIZE);
            byteBuffer.put(val.getBytes(StandardCharsets.UTF_8));
            return;
        }
        byteBuffer.put(position, (byte) 0);
    }

    @Override
    public String readValue(ByteBuffer byteBuffer, Integer position) {

        byte isNull = byteBuffer.get(position);
        if (isNull == 0) {
            return null;
        }
        byteBuffer.position(position + CHECK_NULL_SIZE);
        byte[] read = new byte[byteBuffer.getInt()];
        byteBuffer.position(position + INT_SIZE + CHECK_NULL_SIZE);
        byteBuffer.get(read);
        return new String(read, StandardCharsets.UTF_8);
    }

    @Override
    public Class<String> getClazz() {
        return String.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        if (column.getSize() == 0) {
            throw new DaobabException("Field " + column.getColumn().toString() + " needs size specified.");
        }
        //max utf-8 space has to be multiplied by 6 (worst case scenario)
        return column.getSize() * 6 + INT_SIZE + CHECK_NULL_SIZE;
    }

}
