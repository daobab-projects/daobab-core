package io.daobab.target.buffer.nonheap.access.array;


import io.daobab.model.TableColumn;
import io.daobab.target.buffer.nonheap.access.field.BitFieldDouble;
import io.daobab.target.buffer.nonheap.access.field.BitSize;

public class BitArrayDouble extends BitArrayBase<Double, BitFieldDouble> {

    private final BitFieldDouble instance;

    public BitArrayDouble(TableColumn tableColumn) {
        instance = new BitFieldDouble(tableColumn);
    }

    @Override
    public Double[] createArrayForLength(int length) {
        return new Double[length];
    }

    @Override
    public BitFieldDouble getTypeBitField() {
        return instance;
    }

    @Override
    public int getTypeSize() {
        return BitSize.NULL + BitSize.DOUBLE;
    }

    @Override
    public Class<Double[]> getClazz() {
        return Double[].class;
    }

}
