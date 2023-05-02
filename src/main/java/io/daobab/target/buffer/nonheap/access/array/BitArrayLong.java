package io.daobab.target.buffer.nonheap.access.array;


import io.daobab.model.TableColumn;
import io.daobab.target.buffer.nonheap.access.field.BitFieldLong;
import io.daobab.target.buffer.nonheap.access.field.BitSize;

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

}
