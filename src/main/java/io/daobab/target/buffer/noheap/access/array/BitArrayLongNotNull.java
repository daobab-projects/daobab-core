package io.daobab.target.buffer.noheap.access.array;


import io.daobab.model.TableColumn;
import io.daobab.target.buffer.noheap.access.field.BitFieldLongNotNull;
import io.daobab.target.buffer.noheap.access.field.BitSize;

import java.util.Comparator;

public class BitArrayLongNotNull extends BitArrayBaseNotNull<Long, BitFieldLongNotNull> {

    private final BitFieldLongNotNull instance;

    public BitArrayLongNotNull(TableColumn tableColumn) {
        instance = new BitFieldLongNotNull(tableColumn);
    }

    @Override
    public Long[] createArrayForLength(int length) {
        return new Long[length];
    }

    @Override
    public BitFieldLongNotNull getTypeBitField() {
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
