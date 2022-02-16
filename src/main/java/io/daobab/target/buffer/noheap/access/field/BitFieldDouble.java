package io.daobab.target.buffer.noheap.access.field;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;
import java.util.Comparator;

public class BitFieldDouble extends BitFieldComparable<Double> {

    public BitFieldDouble(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Double aDouble) {
        if (aDouble != null) {
            byteBuffer.put(position, (byte) 1);
            byteBuffer.putDouble(position + 1, aDouble);
            return;
        }
        byteBuffer.put(position, (byte) 0);
    }

    @Override
    public Double readValue(ByteBuffer byteBuffer, Integer position) {
        if (byteBuffer.get(position) == 0) {
            return null;
        }
        return byteBuffer.getDouble(position + 1);
    }

    @Override
    public Class<Double> getClazz() {
        return Double.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.DOUBLE + BitSize.NULL;
    }


    @Override
    public Comparator<? super Double> comparator() {
        return (Comparator<Double>) (o1, o2) -> {
            if (o1 != null && o2 != null) {
                return o1.compareTo(o2);
            }
            if (o1 == null && o2 == null) return 0;
            if (o1 != null) return -1;
            return 1;
        };
    }

}
