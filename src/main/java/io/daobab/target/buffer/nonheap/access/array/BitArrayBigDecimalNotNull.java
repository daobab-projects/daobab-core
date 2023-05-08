package io.daobab.target.buffer.nonheap.access.array;


import io.daobab.model.TableColumn;
import io.daobab.target.buffer.nonheap.access.field.BitFieldBigDecimalNotNull;
import io.daobab.target.buffer.nonheap.access.field.BitSize;

import java.math.BigDecimal;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class BitArrayBigDecimalNotNull extends BitArrayBaseNotNull<BigDecimal, BitFieldBigDecimalNotNull> {

    private final BitFieldBigDecimalNotNull instance;

    private final int fieldLength;

    public BitArrayBigDecimalNotNull(TableColumn tableColumn) {
        this.fieldLength = tableColumn.getSize();
        instance = new BitFieldBigDecimalNotNull(tableColumn);
    }

    @Override
    public BigDecimal[] createArrayForLength(int length) {
        return new BigDecimal[length];
    }

    @Override
    public BitFieldBigDecimalNotNull getTypeBitField() {
        return instance;
    }

    @Override
    public int getTypeSize() {
        return BitSize.calculateBigDecimalSize(fieldLength, false);
    }

    @Override
    public Class<BigDecimal[]> getClazz() {
        return BigDecimal[].class;
    }

}
