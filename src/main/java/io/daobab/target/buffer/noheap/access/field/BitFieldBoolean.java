package io.daobab.target.buffer.noheap.access.field;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;
import java.util.Comparator;

public class BitFieldBoolean extends BitFieldComparable<Boolean> {

    public BitFieldBoolean(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Boolean val) {
        if (val != null) {
            byteBuffer.put(position, (byte) (Boolean.TRUE.equals(val) ? 2 : 1));
//            byteBuffer.put(position + 1, (byte) val);
            return;
        }
        byteBuffer.put(position, (byte) 0); //2 means null in Boolean
    }

    @Override
    public Boolean readValue(ByteBuffer byteBuffer, Integer position) {
        if (byteBuffer.get(position) == 0) { //0 means null
            return null;
        }
        return byteBuffer.get(position + BitSize.NULL) == 2;
    }

    @Override
    public Class<Boolean> getClazz() {
        return Boolean.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return 1 + BitSize.NULL;
    }

    @Override
    public Comparator<? super Boolean> comparator() {
        return (Comparator<Boolean>) (o1, o2) -> {
            if (o1 != null && o2 != null) {
                return o1.compareTo(o2);
            }
            if (o1 == null && o2 == null) return 0;
            if (o1 != null) return -1;
            return 1;
        };
    }

}
