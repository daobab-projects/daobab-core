package io.daobab.target.buffer.noheap.access.array;


import io.daobab.model.TableColumn;
import io.daobab.target.buffer.noheap.access.field.BitFieldDate;
import io.daobab.target.buffer.noheap.access.field.BitSize;

import java.util.Date;

public class BitArrayDate extends BitArrayBase<Date, BitFieldDate> {

    private final BitFieldDate instance;

    public BitArrayDate(TableColumn tableColumn) {
        instance = new BitFieldDate(tableColumn);
    }

    @Override
    public Date[] createArrayForLength(int length) {
        return new Date[length];
    }

    @Override
    public BitFieldDate getTypeBitField() {
        return instance;
    }

    @Override
    public int getTypeSize() {
        return BitSize.DATE_UTIL;
    }

    @Override
    public Class<Date[]> getClazz() {
        return Date[].class;
    }

}
