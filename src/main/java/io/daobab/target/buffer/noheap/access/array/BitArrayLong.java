package io.daobab.target.buffer.noheap.access.array;


import io.daobab.model.TableColumn;
import io.daobab.target.buffer.noheap.access.field.BitFieldLong;
import io.daobab.target.buffer.noheap.access.field.BitSize;

import java.util.Comparator;

public class BitArrayLong extends BitArrayBase<Long, BitFieldLong> {

    private final BitFieldLong instance;

    public BitArrayLong(TableColumn tableColumn) {
        instance = new BitFieldLong(tableColumn);
    }

    @Override
    public Long[] createArrayForLength(int length) {
        return new Long[length];
    }

    @Override
    public BitFieldLong getTypeBitField() {
        return instance;
    }

    @Override
    public int getTypeSize() {
        return BitSize.LONG;
    }

    @Override
    public Class<Long[]> getClazz() {
        return Long[].class;
    }

    @Override
    public Comparator<? super Long> comparator() {
        return Comparator.comparing(Long::valueOf);
    }
}
