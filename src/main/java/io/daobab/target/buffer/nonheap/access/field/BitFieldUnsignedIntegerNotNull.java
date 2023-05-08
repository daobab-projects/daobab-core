package io.daobab.target.buffer.nonheap.access.field;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;
import java.util.Comparator;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class BitFieldUnsignedIntegerNotNull extends BitFieldComparable<Integer> {

    public BitFieldUnsignedIntegerNotNull() {
        this(new EmptyTableColumn());
    }

    public BitFieldUnsignedIntegerNotNull(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Integer val) {
        byteBuffer.position(position);
        byteBuffer.put(new byte[]{
                (byte) (val >> 24),
                (byte) (val >> 16),
                (byte) (val >> 8),
                (byte) (val & 0xff)});
    }


    @Override
    public Integer readValue(ByteBuffer byteBuffer, Integer position) {
        return (byteBuffer.get(position) & 0xFF) << 24 |
                ((byteBuffer.get(position + 1) & 0xFF) << 16) |
                ((byteBuffer.get(position + 2) & 0xFF) << 8) |
                (byteBuffer.get(position + 3) & 0xFF);
    }

    @Override
    public Class<Integer> getClazz() {
        return Integer.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.UNSIGNED_INT;
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
