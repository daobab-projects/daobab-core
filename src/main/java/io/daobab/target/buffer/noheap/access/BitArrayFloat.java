package io.daobab.target.buffer.noheap.access;


public class BitArrayFloat extends BitArrayBase<Float, BitFieldFloat> {

    private final BitFieldFloat instance = new BitFieldFloat();

    @Override
    protected Float[] createArrayForLength(int length) {
        return new Float[length];
    }

    @Override
    protected BitFieldFloat getTypeBitField() {
        return instance;
    }

    @Override
    protected int getTypeSize() {
        return CHECK_NULL_SIZE + FLOAT_SIZE;
    }

    @Override
    public Class<Float[]> getClazz() {
        return Float[].class;
    }

}
