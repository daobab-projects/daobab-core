package io.daobab.target.buffer.nonheap.access.array;


import io.daobab.model.TableColumn;
import io.daobab.target.buffer.nonheap.access.field.BitFieldIntegerNotNull;
import io.daobab.target.buffer.nonheap.access.field.BitSize;

public class BitArrayIntegerNotNull extends BitArrayBaseNotNull<Integer, BitFieldIntegerNotNull> {

    private final BitFieldIntegerNotNull instance;

    public BitArrayIntegerNotNull(TableColumn tableColumn) {
        instance = new BitFieldIntegerNotNull(tableColumn);
    }

    @Override
    public Integer[] createArrayForLength(int length) {
        return new Integer[length];
    }

    @Override
    public BitFieldIntegerNotNull getTypeBitField() {
        return instance;
    }

    @Override
    public int getTypeSize() {
        return BitSize.INT;
    }

    @Override
    public Class<Integer[]> getClazz() {
        return Integer[].class;
    }

}
