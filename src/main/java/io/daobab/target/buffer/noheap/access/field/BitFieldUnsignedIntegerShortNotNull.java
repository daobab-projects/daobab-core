package io.daobab.target.buffer.noheap.access.field;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BitFieldUnsignedIntegerShortNotNull extends BitFieldComparable<Integer> {

    public BitFieldUnsignedIntegerShortNotNull() {
        this(new EmptyTableColumn());
    }

    public BitFieldUnsignedIntegerShortNotNull(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Integer val) {
        byteBuffer.position(position);
        byteBuffer.put(new byte[]{
                (byte) (val >> 8),
                (byte) (val & 0xff)});
    }


    @Override
    public Integer readValue(ByteBuffer byteBuffer, Integer position) {
        return ((byteBuffer.get(position) & 0xFF) << 8) | (byteBuffer.get(position + 1) & 0xFF);
    }

    public List<Integer> readList(ByteBuffer byteBuffer, Integer position, int length) {
        List<Integer> rv = new ArrayList<>(length);
        int maxpos = position + length + length;
        while (position < maxpos) {
            rv.add((byteBuffer.get(position) & 0xFF << 8) | (byteBuffer.get(position + 1) & 0xFF));
            position += 2;
        }
        return rv;
    }

    @Override
    public Class<Integer> getClazz() {
        return Integer.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.UNSIGNED_SHORT;
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
