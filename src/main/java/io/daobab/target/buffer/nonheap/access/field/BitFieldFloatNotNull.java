package io.daobab.target.buffer.nonheap.access.field;

import io.daobab.model.TableColumn;

import java.nio.ByteBuffer;
import java.util.Comparator;

public class BitFieldFloatNotNull extends BitFieldComparable<Float> {

    public BitFieldFloatNotNull(TableColumn tableColumn) {
    }

    @Override
    public void writeValue(ByteBuffer byteBuffer, Integer position, Float val) {
        byteBuffer.putFloat(position, val);
    }

    @Override
    public Float readValue(ByteBuffer byteBuffer, Integer position) {
        return byteBuffer.getFloat(position);
    }

    @Override
    public Class<Float> getClazz() {
        return Float.class;
    }

    @Override
    public int calculateSpace(TableColumn column) {
        return BitSize.FLOAT;
    }

    @Override
    public Comparator<? super Float> comparator() {
        return (Comparator<Float>) (o1, o2) -> {
            if (o1 != null && o2 != null) {
                return o1.compareTo(o2);
            }
            if (o1 == null && o2 == null) return 0;
            if (o1 != null) return -1;
            return 1;
        };
    }

}
