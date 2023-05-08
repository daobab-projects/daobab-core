package io.daobab.target.buffer.nonheap.access.field;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;
import java.util.Comparator;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class BitFieldInteger extends BitFieldComparable<Integer> {

    public BitFieldInteger() {
    }

    public BitFieldInteger(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Integer val) {

        if (val != null) {
            byteBuffer.put(position, (byte) 1);
            byteBuffer.putInt(position + BitSize.NULL, val);
            return;
        }
        byteBuffer.put(position, (byte) 0);

    }

    @Override
    public Integer readValue(ByteBuffer byteBuffer, Integer position) {
        if (byteBuffer.get(position) == 0) {
            return null;
        }
        return byteBuffer.getInt(position + BitSize.NULL);
    }

    @Override
    public Class<Integer> getClazz() {
        return Integer.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.INT + BitSize.NULL;
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
