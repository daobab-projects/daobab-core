package io.daobab.target.buffer.noheap.access;


public class BitArrayString extends BitArrayBase<String, BitFieldString> {

    private final BitFieldString instance = new BitFieldString();

    private final int stringLength;

    public BitArrayString(int stringLength) {
        this.stringLength = stringLength;
    }

    @Override
    protected String[] createArrayForLength(int length) {
        return new String[length];
    }

    @Override
    protected BitFieldString getTypeBitField() {
        return instance;
    }

    @Override
    protected int getTypeSize() {
        return CHECK_NULL_SIZE + (stringLength * 6 + INT_SIZE + CHECK_NULL_SIZE);
    }

    @Override
    public Class<String[]> getClazz() {
        return String[].class;
    }

}
