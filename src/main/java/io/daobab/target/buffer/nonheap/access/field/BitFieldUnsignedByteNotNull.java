package io.daobab.target.buffer.nonheap.access.field;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;
import java.util.Comparator;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class BitFieldUnsignedByteNotNull extends BitFieldComparable<Integer> {

    public BitFieldUnsignedByteNotNull() {
        this(new EmptyTableColumn());
    }

    public BitFieldUnsignedByteNotNull(TableColumn tableColumn) {
    }

    public void writeNullInformation(ByteBuffer byteBuffer, Integer position) {
        writeValue(byteBuffer, position, 0);
    }

    public void writeNotNullInformation(ByteBuffer byteBuffer, Integer position) {
        writeValue(byteBuffer,position,1);
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Integer val) {
        byteBuffer.position(position);
        byteBuffer.put((byte)(val & 0xff));
    }

    public boolean isNull(ByteBuffer byteBuffer, Integer position) {
        return (byteBuffer.get(position) & 0xFF) == 0;
    }

    @Override
    public Integer readValue(ByteBuffer byteBuffer, Integer position) {
        return byteBuffer.get(position) & 0xFF;
    }

    @Override
    public Class<Integer> getClazz() {
        return Integer.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.UNSIGNED_BYTE;
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
