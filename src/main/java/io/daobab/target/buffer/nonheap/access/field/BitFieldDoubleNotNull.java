package io.daobab.target.buffer.nonheap.access.field;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;
import java.util.Comparator;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class BitFieldDoubleNotNull extends BitFieldComparable<Double> {

    public BitFieldDoubleNotNull(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Double val) {
        byteBuffer.putDouble(position, val);
    }

    @Override
    public Double readValue(ByteBuffer byteBuffer, Integer position) {
        return byteBuffer.getDouble(position);
    }

    @Override
    public Class<Double> getClazz() {
        return Double.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.DOUBLE;
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
