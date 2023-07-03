package io.daobab.target.buffer.nonheap.access.array;


import io.daobab.model.TableColumn;
import io.daobab.target.buffer.nonheap.access.field.BitFieldLocalDateTimeNotNull;
import io.daobab.target.buffer.nonheap.access.field.BitSize;

import java.time.LocalDateTime;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class BitArrayLocalDateTimeNotNull extends BitArrayBaseNotNull<LocalDateTime, BitFieldLocalDateTimeNotNull> {

    private final BitFieldLocalDateTimeNotNull instance;


    public BitArrayLocalDateTimeNotNull(TableColumn tableColumn) {
        instance = new BitFieldLocalDateTimeNotNull(tableColumn);
    }

    @Override
    public LocalDateTime[] createArrayForLength(int length) {
        return new LocalDateTime[length];
    }

    @Override
    public BitFieldLocalDateTimeNotNull getTypeBitField() {
        return instance;
    }

    @Override
    public int getTypeSize() {
        return BitSize.DOUBLE;
    }

    @Override
    public Class<LocalDateTime[]> getClazz() {
        return LocalDateTime[].class;
    }

}
