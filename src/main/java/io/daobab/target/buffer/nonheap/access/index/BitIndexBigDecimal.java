package io.daobab.target.buffer.nonheap.access.index;

import io.daobab.model.TableColumn;
import io.daobab.target.buffer.nonheap.access.array.BitArrayBigDecimalNotNull;
import io.daobab.target.buffer.nonheap.access.field.BitFieldBigDecimalNotNull;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class BitIndexBigDecimal extends BitIndex<BigDecimal, BitFieldBigDecimalNotNull, BitIndexBigDecimal> {

    public BitIndexBigDecimal(final TableColumn tableColumn, SortedMap<BigDecimal, Collection<Integer>> valueIndex, List<Integer> nullValues) {
        super(tableColumn, valueIndex, nullValues, new BitArrayBigDecimalNotNull(tableColumn));
    }

    @Override
    protected BitIndexBigDecimal createInstance(final TableColumn tableColumn, SortedMap<BigDecimal, Collection<Integer>> valueIndex, List<Integer> nullValues) {
        return new BitIndexBigDecimal(tableColumn, valueIndex, nullValues);
    }

    protected BitIndexBigDecimal(BitIndexBigDecimal original, TableColumn tableColumn, Collection<BigDecimal> keys, boolean nullValues) {
        super(original, tableColumn, keys, nullValues);
    }

    @Override
    protected BitIndexBigDecimal createSubIndex(BitIndexBigDecimal original, TableColumn tableColumn, Collection<BigDecimal> keys, boolean nullValues) {
        return new BitIndexBigDecimal(original, tableColumn, keys, nullValues);
    }

}
