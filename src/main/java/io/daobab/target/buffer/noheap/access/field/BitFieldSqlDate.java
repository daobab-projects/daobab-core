package io.daobab.target.buffer.noheap.access.field;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;
import java.sql.Date;

public class BitFieldSqlDate implements BitField<Date> {

    public BitFieldSqlDate(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Date val) {
        if (val != null) {
            byteBuffer.put(position, (byte) 1);
            byteBuffer.putLong(position + BitSize.CHECK_NULL, val.getTime());
            return;
        }
        byteBuffer.put(position, (byte) 0);
    }

    @Override
    public Date readValue(ByteBuffer byteBuffer, Integer position) {
        if (byteBuffer.get(position) == 0) {
            return null;
        }
        return new Date(byteBuffer.getLong(position + BitSize.CHECK_NULL));
    }

    @Override
    public Class<Date> getClazz() {
        return Date.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.LONG + BitSize.CHECK_NULL;
    }

}
