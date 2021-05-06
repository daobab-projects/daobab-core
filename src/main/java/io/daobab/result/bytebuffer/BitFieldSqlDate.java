package io.daobab.result.bytebuffer;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;
import java.sql.Date;

public class BitFieldSqlDate extends BitField<Date> {

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Object val) {
        if (val != null) {
            byteBuffer.put(position, (byte) 1);
            Date tval = (Date) val;
            byteBuffer.putLong(position + CHECK_NULL_SIZE, tval.getTime());
            return;
        }
        byteBuffer.put(position, (byte) 0);
    }

    @Override
    public Date readValue(ByteBuffer byteBuffer, Integer position) {
        if (byteBuffer.get(position) == 0) {
            return null;
        }
        return new Date(byteBuffer.getLong(position + CHECK_NULL_SIZE));
    }

    @Override
    public Class<Date> getClazz() {
        return Date.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return LONG_SIZE + CHECK_NULL_SIZE;
    }

}
