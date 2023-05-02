package io.daobab.target.buffer.nonheap.access.array;


import io.daobab.model.TableColumn;
import io.daobab.target.buffer.nonheap.access.field.BitFieldShortNotNull;
import io.daobab.target.buffer.nonheap.access.field.BitSize;

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

}
