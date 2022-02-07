package io.daobab.target.buffer.noheap.access;


public class BitArrayDouble extends BitArrayBase<Double, BitFieldDouble> {

    private final BitFieldDouble instance = new BitFieldDouble();

    @Override
    protected Double[] createArrayForLength(int length) {
        return new Double[length];
    }

    @Override
    protected BitFieldDouble getTypeBitField() {
        return instance;
    }

    @Override
    protected int getTypeSize() {
        return CHECK_NULL_SIZE + DOUBLE_SIZE;
    }

    @Override
    public Class<Double[]> getClazz() {
        return Double[].class;
    }

}
