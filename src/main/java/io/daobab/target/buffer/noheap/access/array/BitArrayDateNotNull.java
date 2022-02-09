package io.daobab.target.buffer.noheap.access.array;


import io.daobab.model.TableColumn;
import io.daobab.target.buffer.noheap.access.field.BitFieldDateNotNull;
import io.daobab.target.buffer.noheap.access.field.BitSize;

import java.util.Comparator;
import java.util.Date;

public class BitArrayDateNotNull extends BitArrayBaseNotNull<Date, BitFieldDateNotNull> {

    private final BitFieldDateNotNull instance;

    public BitArrayDateNotNull(TableColumn tableColumn) {
        instance = new BitFieldDateNotNull(tableColumn);
    }

    @Override
    public Date[] createArrayForLength(int length) {
        return new Date[length];
    }

    @Override
    public BitFieldDateNotNull getTypeBitField() {
        return instance;
    }

    @Override
    public int getTypeSize() {
        return BitSize.DATE_UTIL;
    }

    @Override
    public Comparator<? super Date> comparator() {
        return Comparator.comparing(Date::getTime);
    }

    @Override
    public Class<Date[]> getClazz() {
        return Date[].class;
    }


}
