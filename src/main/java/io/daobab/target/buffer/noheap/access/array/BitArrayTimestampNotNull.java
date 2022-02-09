package io.daobab.target.buffer.noheap.access.array;


import io.daobab.model.TableColumn;
import io.daobab.target.buffer.noheap.access.field.BitFieldTimestampNotNull;
import io.daobab.target.buffer.noheap.access.field.BitSize;

import java.sql.Timestamp;
import java.util.Comparator;

public class BitArrayTimestampNotNull extends BitArrayBaseNotNull<Timestamp, BitFieldTimestampNotNull> {

    private final BitFieldTimestampNotNull instance;

    public BitArrayTimestampNotNull(TableColumn tableColumn) {
        instance = new BitFieldTimestampNotNull(tableColumn);
    }

    @Override
    public Timestamp[] createArrayForLength(int length) {
        return new Timestamp[length];
    }

    @Override
    public BitFieldTimestampNotNull getTypeBitField() {
        return instance;
    }

    @Override
    public int getTypeSize() {
        return BitSize.TIMESTAMP_UTIL;
    }

    @Override
    public Class<Timestamp[]> getClazz() {
        return Timestamp[].class;
    }

    @Override
    public Comparator<? super Timestamp> comparator() {
        return Comparator.comparing(Timestamp::getTime);
    }
}
