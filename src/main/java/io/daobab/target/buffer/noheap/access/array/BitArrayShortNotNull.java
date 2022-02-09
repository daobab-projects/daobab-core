package io.daobab.target.buffer.noheap.access.array;


import io.daobab.model.TableColumn;
import io.daobab.target.buffer.noheap.access.field.BitFieldShortNotNull;
import io.daobab.target.buffer.noheap.access.field.BitSize;

import java.util.Comparator;

public class BitArrayShortNotNull extends BitArrayBaseNotNull<Short, BitFieldShortNotNull> {

    private final BitFieldShortNotNull instance;

    public BitArrayShortNotNull(TableColumn tableColumn) {
        instance = new BitFieldShortNotNull(tableColumn);
    }

    @Override
    public Short[] createArrayForLength(int length) {
        return new Short[length];
    }

    @Override
    public BitFieldShortNotNull getTypeBitField() {
        return instance;
    }

    @Override
    public int getTypeSize() {
        return BitSize.SHORT;
    }

    @Override
    public Class<Short[]> getClazz() {
        return Short[].class;
    }

    @Override
    public Comparator<? super Short> comparator() {
        return Comparator.comparing(Short::valueOf);
    }
}
