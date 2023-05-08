package io.daobab.target.buffer.nonheap.access.array;


import io.daobab.model.TableColumn;
import io.daobab.target.buffer.nonheap.access.field.BitFieldLongNotNull;
import io.daobab.target.buffer.nonheap.access.field.BitSize;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
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

}
