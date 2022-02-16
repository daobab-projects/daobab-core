package io.daobab.target.buffer.noheap.access.array;


import io.daobab.model.TableColumn;
import io.daobab.target.buffer.noheap.access.field.BitFieldString;
import io.daobab.target.buffer.noheap.access.field.BitSize;

public class BitArrayString extends BitArrayBase<String, BitFieldString> {

    private final BitFieldString instance;
    private final int fieldLength;

    public BitArrayString(final TableColumn tableColumn) {
        instance = new BitFieldString(tableColumn);
        this.fieldLength = tableColumn.getSize();
    }

    @Override
    public String[] createArrayForLength(int length) {
        return new String[length];
    }

    @Override
    public BitFieldString getTypeBitField() {
        return instance;
    }

    @Override
    public int getTypeSize() {
        return BitSize.NULL + (fieldLength * 6 + BitSize.INT + BitSize.NULL);
    }

    @Override
    public Class<String[]> getClazz() {
        return String[].class;
    }

}
