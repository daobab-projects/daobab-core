package io.daobab.target.buffer.noheap.access.field;

import io.daobab.error.DaobabException;
import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class BitFieldString implements BitField<String> {

    public BitFieldString(TableColumn tableColumn) {

    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, String val) {

        if (val != null) {
            byteBuffer.put(position, (byte) 1);

            byteBuffer.putInt(position + BitSize.CHECK_NULL, val.length());
            byteBuffer.position(position + BitSize.INT + BitSize.CHECK_NULL);
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
        byteBuffer.position(position + BitSize.CHECK_NULL);
        byte[] read = new byte[byteBuffer.getInt()];
        byteBuffer.position(position + BitSize.INT + BitSize.CHECK_NULL);
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
//        return column.getSize() * 6 + BitSize.INT + BitSize.CHECK_NULL;
        return BitSize.calculateStringSize(column.getSize(), true);
    }

}
