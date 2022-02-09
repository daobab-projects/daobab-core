package io.daobab.target.buffer.noheap.access.array;


import io.daobab.model.TableColumn;
import io.daobab.target.buffer.noheap.access.field.BitFieldInteger;
import io.daobab.target.buffer.noheap.access.field.BitSize;

import java.util.Comparator;

public class BitArrayInteger extends BitArrayBase<Integer, BitFieldInteger> {

    private final BitFieldInteger instance;

    public BitArrayInteger(TableColumn tableColumn) {
        instance = new BitFieldInteger(tableColumn);
    }

    @Override
    public Integer[] createArrayForLength(int length) {
        return new Integer[length];
    }

    @Override
    public BitFieldInteger getTypeBitField() {
        return instance;
    }

    @Override
    public int getTypeSize() {
        return BitSize.CHECK_NULL + BitSize.INT;
    }

    @Override
    public Class<Integer[]> getClazz() {
        return Integer[].class;
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return Comparator.comparing(Integer::valueOf);
    }

}
