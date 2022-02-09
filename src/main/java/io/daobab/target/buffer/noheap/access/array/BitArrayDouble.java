package io.daobab.target.buffer.noheap.access.array;


import io.daobab.model.TableColumn;
import io.daobab.target.buffer.noheap.access.field.BitFieldDouble;
import io.daobab.target.buffer.noheap.access.field.BitSize;

import java.util.Comparator;

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
        return BitSize.CHECK_NULL + BitSize.DOUBLE;
    }

    @Override
    public Class<Double[]> getClazz() {
        return Double[].class;
    }

    @Override
    public Comparator<? super Double> comparator() {
        return Comparator.comparing(Double::valueOf);
    }

}
