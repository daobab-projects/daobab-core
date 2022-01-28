package io.daobab.target.buffer.noheap.access.field;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;
import java.util.Comparator;

public class BitFieldLongNotNull extends BitFieldComparable<Long> {

    public BitFieldLongNotNull(TableColumn tableColumn) {
    }


    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Long val) {
        byteBuffer.putLong(position, val);
    }

    @Override
    public Long readValue(ByteBuffer byteBuffer, Integer position) {
        return byteBuffer.getLong(position);
    }

    @Override
    public Class<Long> getClazz() {
        return Long.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.LONG;
    }


    @Override
    public Comparator<? super Long> comparator() {
        return (Comparator<Long>) (o1, o2) -> {
            if (o1 != null && o2 != null) {
                return o1.compareTo(o2);
            }
            if (o1 == null && o2 == null) return 0;
            if (o1 != null) return -1;
            return 1;
        };
    }
}
