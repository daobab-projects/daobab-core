package io.daobab.target.buffer.nonheap.access.array;


import io.daobab.model.TableColumn;
import io.daobab.target.buffer.nonheap.access.field.BitFieldTimestampNotNull;
import io.daobab.target.buffer.nonheap.access.field.BitSize;

import java.sql.Timestamp;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
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
