package io.daobab.target.buffer.noheap.access.array;


import io.daobab.model.TableColumn;
import io.daobab.target.buffer.noheap.access.field.BitFieldTimestamp;
import io.daobab.target.buffer.noheap.access.field.BitSize;

import java.sql.Timestamp;

public class BitArrayTimestamp extends BitArrayBase<Timestamp, BitFieldTimestamp> {

    private final BitFieldTimestamp instance;

    public BitArrayTimestamp(TableColumn tableColumn) {
        instance = new BitFieldTimestamp(tableColumn);
    }

    @Override
    public Timestamp[] createArrayForLength(int length) {
        return new Timestamp[length];
    }

    @Override
    public BitFieldTimestamp getTypeBitField() {
        return instance;
    }

    @Override
    public int getTypeSize() {
        return BitSize.TIMESTAMP_SQL;
    }

    @Override
    public Class<Timestamp[]> getClazz() {
        return Timestamp[].class;
    }

}
