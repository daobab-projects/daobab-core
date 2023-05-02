package io.daobab.target.buffer.nonheap.access.array;


import io.daobab.model.TableColumn;
import io.daobab.target.buffer.nonheap.access.field.BitFieldFloat;
import io.daobab.target.buffer.nonheap.access.field.BitSize;

public class BitArrayFloat extends BitArrayBase<Float, BitFieldFloat> {

    private final BitFieldFloat instance;

    public BitArrayFloat(TableColumn tableColumn) {
        instance = new BitFieldFloat(tableColumn);
    }

    @Override
    public Float[] createArrayForLength(int length) {
        return new Float[length];
    }

    @Override
    public BitFieldFloat getTypeBitField() {
        return instance;
    }

    @Override
    public int getTypeSize() {
        return BitSize.NULL + BitSize.FLOAT;
    }

    @Override
    public Class<Float[]> getClazz() {
        return Float[].class;
    }


}
