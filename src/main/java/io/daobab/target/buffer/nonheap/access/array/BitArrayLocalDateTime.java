package io.daobab.target.buffer.nonheap.access.array;


import io.daobab.model.TableColumn;
import io.daobab.target.buffer.nonheap.access.field.BitFieldLocalDateTime;
import io.daobab.target.buffer.nonheap.access.field.BitSize;

import java.time.LocalDateTime;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class BitArrayLocalDateTime extends BitArrayBase<LocalDateTime, BitFieldLocalDateTime> {

    private final BitFieldLocalDateTime instance;

    public BitArrayLocalDateTime(TableColumn tableColumn) {
        instance = new BitFieldLocalDateTime(tableColumn);
    }

    @Override
    public LocalDateTime[] createArrayForLength(int length) {
        return new LocalDateTime[length];
    }

    @Override
    public BitFieldLocalDateTime getTypeBitField() {
        return instance;
    }

    @Override
    public int getTypeSize() {
        return BitSize.NULL + BitSize.DATE_UTIL;
    }

    @Override
    public Class<LocalDateTime[]> getClazz() {
        return LocalDateTime[].class;
    }

}
