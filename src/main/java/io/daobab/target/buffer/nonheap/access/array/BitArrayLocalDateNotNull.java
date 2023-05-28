package io.daobab.target.buffer.nonheap.access.array;


import io.daobab.model.TableColumn;
import io.daobab.target.buffer.nonheap.access.field.BitFieldLocalDateNotNull;
import io.daobab.target.buffer.nonheap.access.field.BitSize;

import java.time.LocalDate;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class BitArrayLocalDateNotNull extends BitArrayBaseNotNull<LocalDate, BitFieldLocalDateNotNull> {

    private final BitFieldLocalDateNotNull instance;


    public BitArrayLocalDateNotNull(TableColumn tableColumn) {
        instance = new BitFieldLocalDateNotNull(tableColumn);
    }

    @Override
    public LocalDate[] createArrayForLength(int length) {
        return new LocalDate[length];
    }

    @Override
    public BitFieldLocalDateNotNull getTypeBitField() {
        return instance;
    }

    @Override
    public int getTypeSize() {
        return BitSize.DOUBLE;
    }

    @Override
    public Class<LocalDate[]> getClazz() {
        return LocalDate[].class;
    }

}
