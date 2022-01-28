package io.daobab.target.buffer.noheap.access.array;


import io.daobab.model.TableColumn;
import io.daobab.target.buffer.noheap.access.field.BitFieldFloatNotNull;
import io.daobab.target.buffer.noheap.access.field.BitSize;

public class BitArrayFloatNotNull extends BitArrayBaseNotNull<Float, BitFieldFloatNotNull> {

    private final BitFieldFloatNotNull instance;

    public BitArrayFloatNotNull(TableColumn tableColumn) {
        instance = new BitFieldFloatNotNull(tableColumn);
    }

    @Override
    public Float[] createArrayForLength(int length) {
        return new Float[length];
    }

    @Override
    public BitFieldFloatNotNull getTypeBitField() {
        return instance;
    }

    @Override
    public int getTypeSize() {
        return BitSize.FLOAT;
    }

    @Override
    public Class<Float[]> getClazz() {
        return Float[].class;
    }

}
