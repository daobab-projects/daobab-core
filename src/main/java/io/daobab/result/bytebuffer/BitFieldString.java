package io.daobab.result.bytebuffer;

import io.daobab.error.DaobabException;
import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class BitFieldString extends BitField<String> {

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Object value) {

        if (value != null) {
            byteBuffer.put(position, (byte) 1);
            String val = (String) value;

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
        return column.getSize() + INT_SIZE + CHECK_NULL_SIZE;
    }

}
