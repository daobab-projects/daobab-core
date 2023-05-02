package io.daobab.target.buffer.nonheap.access.array;


import io.daobab.model.TableColumn;
import io.daobab.target.buffer.nonheap.access.field.BitFieldUnsignedIntegerNotNull;
import io.daobab.target.buffer.nonheap.access.field.BitSize;

public class BitArrayUnsignedIntegerNotNull extends BitArrayBaseNotNull<Integer, BitFieldUnsignedIntegerNotNull> {

    private final BitFieldUnsignedIntegerNotNull instance;

    public BitArrayUnsignedIntegerNotNull(TableColumn tableColumn) {
        instance = new BitFieldUnsignedIntegerNotNull(tableColumn);
    }

    @Override
    public Integer[] createArrayForLength(int length) {
        return new Integer[length];
    }

    @Override
    public BitFieldUnsignedIntegerNotNull getTypeBitField() {
        return instance;
    }

    @Override
    public int getTypeSize() {
        return BitSize.UNSIGNED_INT;
    }

    @Override
    public Class<Integer[]> getClazz() {
        return Integer[].class;
    }


}
