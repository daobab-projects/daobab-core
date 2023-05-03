package io.daobab.target.buffer.nonheap.access.array;


import io.daobab.model.TableColumn;
import io.daobab.target.buffer.nonheap.access.field.BitFieldBigDecimal;
import io.daobab.target.buffer.nonheap.access.field.BitSize;

import java.math.BigDecimal;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class BitArrayBigDecimal extends BitArrayBase<BigDecimal, BitFieldBigDecimal> {

    private final BitFieldBigDecimal instance;

    private final int fieldLength;

    public BitArrayBigDecimal(TableColumn tableColumn) {
        this.fieldLength = tableColumn.getSize();
        instance = new BitFieldBigDecimal(tableColumn);
    }

    @Override
    public BigDecimal[] createArrayForLength(int length) {
        return new BigDecimal[length];
    }

    @Override
    public BitFieldBigDecimal getTypeBitField() {
        return instance;
    }

    @Override
    public int getTypeSize() {
        return BitSize.calculateBigDecimalSize(fieldLength, true);
    }

    @Override
    public Class<BigDecimal[]> getClazz() {
        return BigDecimal[].class;
    }


//    @Override
//    public Comparator<? super BigDecimal> comparator() {
//        return Comparator.comparing(BigDecimal::longValue);
//    }
}
