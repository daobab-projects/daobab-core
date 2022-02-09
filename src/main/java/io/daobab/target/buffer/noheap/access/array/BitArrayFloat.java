package io.daobab.target.buffer.noheap.access.array;


import io.daobab.model.TableColumn;
import io.daobab.target.buffer.noheap.access.field.BitFieldFloat;
import io.daobab.target.buffer.noheap.access.field.BitSize;

import java.util.Comparator;

public class BitArrayFloat extends BitArrayBase<Float, BitFieldFloat> {

    private final BitFieldFloat instance;

    public BitArrayFloat(TableColumn tableColumn) {
        instance = new BitFieldFloat(tableColumn);
    }

    @Override
    public Float[] createArrayForLength(int length) {
        return new Float[length];
    }

    @Override
    public BitFieldFloat getTypeBitField() {
        return instance;
    }

    @Override
    public int getTypeSize() {
        return BitSize.CHECK_NULL + BitSize.FLOAT;
    }

    @Override
    public Class<Float[]> getClazz() {
        return Float[].class;
    }

    @Override
    public Comparator<? super Float> comparator() {
        return Comparator.comparing(Float::valueOf);
    }

}
