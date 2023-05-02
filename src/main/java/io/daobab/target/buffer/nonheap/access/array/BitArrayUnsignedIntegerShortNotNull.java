package io.daobab.target.buffer.nonheap.access.array;


import io.daobab.model.TableColumn;
import io.daobab.target.buffer.nonheap.access.field.BitFieldUnsignedIntegerShortNotNull;
import io.daobab.target.buffer.nonheap.access.field.BitSize;

public class BitArrayUnsignedIntegerShortNotNull extends BitArrayBaseNotNull<Integer, BitFieldUnsignedIntegerShortNotNull> {

    private final BitFieldUnsignedIntegerShortNotNull instance;

    public BitArrayUnsignedIntegerShortNotNull(TableColumn tableColumn) {
        instance = new BitFieldUnsignedIntegerShortNotNull(tableColumn);
    }

    @Override
    public Integer[] createArrayForLength(int length) {
        return new Integer[length];
    }

    @Override
    public BitFieldUnsignedIntegerShortNotNull getTypeBitField() {
        return instance;
    }

    @Override
    public int getTypeSize() {
        return BitSize.UNSIGNED_SHORT;
    }

    @Override
    public Class<Integer[]> getClazz() {
        return Integer[].class;
    }


}
