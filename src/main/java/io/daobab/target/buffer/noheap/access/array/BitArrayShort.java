package io.daobab.target.buffer.noheap.access.array;


import io.daobab.model.TableColumn;
import io.daobab.target.buffer.noheap.access.field.BitFieldShort;
import io.daobab.target.buffer.noheap.access.field.BitSize;

import java.util.Comparator;

public class BitArrayShort extends BitArrayBase<Short, BitFieldShort> {

    private final BitFieldShort instance;

    public BitArrayShort(TableColumn tableColumn) {
        instance = new BitFieldShort(tableColumn);
    }

    @Override
    public Short[] createArrayForLength(int length) {
        return new Short[length];
    }

    @Override
    public BitFieldShort getTypeBitField() {
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
