package io.daobab.target.buffer.noheap.access.array;


import io.daobab.model.TableColumn;
import io.daobab.target.buffer.noheap.access.field.BitFieldString;
import io.daobab.target.buffer.noheap.access.field.BitSize;

import java.util.Comparator;

public class BitArrayString extends BitArrayBase<String, BitFieldString> {

    private final BitFieldString instance;
    private final int stringLength;

    public BitArrayString(final TableColumn tableColumn) {
        instance = new BitFieldString(tableColumn);
        this.stringLength = tableColumn.getSize();
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
        return BitSize.CHECK_NULL + (stringLength * 6 + BitSize.INT + BitSize.CHECK_NULL);
    }

    @Override
    public Class<String[]> getClazz() {
        return String[].class;
    }

    @Override
    public Comparator<? super String> comparator() {
        return Comparator.comparing(String::valueOf);
    }
}
