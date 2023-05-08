package io.daobab.target.buffer.nonheap.access.array;


import io.daobab.model.TableColumn;
import io.daobab.target.buffer.nonheap.access.field.BitFieldFloatNotNull;
import io.daobab.target.buffer.nonheap.access.field.BitSize;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
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
