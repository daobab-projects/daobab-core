package io.daobab.target.buffer.nonheap.access.field;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;
import java.util.Comparator;

public class BitFieldIntegerNotNull extends BitFieldComparable<Integer> {

    public BitFieldIntegerNotNull() {
        this(new EmptyTableColumn());
    }

    public BitFieldIntegerNotNull(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Integer val) {
        byteBuffer.putInt(position, val);
    }

    @Override
    public Integer readValue(ByteBuffer byteBuffer, Integer position) {
        return byteBuffer.getInt(position);
    }

    @Override
    public Class<Integer> getClazz() {
        return Integer.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.INT;
    }


    @Override
    public Comparator<? super Integer> comparator() {
        return (Comparator<Integer>) (o1, o2) -> {
            if (o1 != null && o2 != null) {
                return o1.compareTo(o2);
            }
            if (o1 == null && o2 == null) return 0;
            if (o1 != null) return -1;
            return 1;
        };
    }

}
