package io.daobab.target.buffer.noheap.access.array;


import io.daobab.model.TableColumn;
import io.daobab.target.buffer.noheap.access.field.BitFieldDoubleNotNull;
import io.daobab.target.buffer.noheap.access.field.BitSize;

public class BitArrayDoubleNotNull extends BitArrayBaseNotNull<Double, BitFieldDoubleNotNull> {

    private final BitFieldDoubleNotNull instance;


    public BitArrayDoubleNotNull(TableColumn tableColumn) {
        instance = new BitFieldDoubleNotNull(tableColumn);
    }

    @Override
    public Double[] createArrayForLength(int length) {
        return new Double[length];
    }

    @Override
    public BitFieldDoubleNotNull getTypeBitField() {
        return instance;
    }

    @Override
    public int getTypeSize() {
        return BitSize.DOUBLE;
    }

    @Override
    public Class<Double[]> getClazz() {
        return Double[].class;
    }

}
