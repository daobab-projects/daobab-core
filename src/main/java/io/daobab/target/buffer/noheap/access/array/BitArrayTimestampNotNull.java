package io.daobab.target.buffer.noheap.access.array;


import io.daobab.model.TableColumn;
import io.daobab.target.buffer.noheap.access.field.BitFieldTimestampNotNull;
import io.daobab.target.buffer.noheap.access.field.BitSize;

import java.sql.Timestamp;

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
        return BitSize.TIMESTAMP_SQL;
    }

    @Override
    public Class<Timestamp[]> getClazz() {
        return Timestamp[].class;
    }

}
