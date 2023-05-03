package io.daobab.target.buffer.nonheap.access.array;


import io.daobab.model.TableColumn;
import io.daobab.target.buffer.nonheap.access.field.BitFieldShort;
import io.daobab.target.buffer.nonheap.access.field.BitSize;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
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

}
