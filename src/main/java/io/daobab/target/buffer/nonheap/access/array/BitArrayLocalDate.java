package io.daobab.target.buffer.nonheap.access.array;


import io.daobab.model.TableColumn;
import io.daobab.target.buffer.nonheap.access.field.BitFieldLocalDate;
import io.daobab.target.buffer.nonheap.access.field.BitSize;

import java.time.LocalDate;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class BitArrayLocalDate extends BitArrayBase<LocalDate, BitFieldLocalDate> {

    private final BitFieldLocalDate instance;

    public BitArrayLocalDate(TableColumn tableColumn) {
        instance = new BitFieldLocalDate(tableColumn);
    }

    @Override
    public LocalDate[] createArrayForLength(int length) {
        return new LocalDate[length];
    }

    @Override
    public BitFieldLocalDate getTypeBitField() {
        return instance;
    }

    @Override
    public int getTypeSize() {
        return BitSize.NULL + BitSize.DATE_UTIL;
    }

    @Override
    public Class<LocalDate[]> getClazz() {
        return LocalDate[].class;
    }

}
