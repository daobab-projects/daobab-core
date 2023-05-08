package io.daobab.target.buffer.nonheap.access.index;

import io.daobab.model.TableColumn;
import io.daobab.target.buffer.nonheap.access.array.BitArrayDateNotNull;
import io.daobab.target.buffer.nonheap.access.field.BitFieldDateNotNull;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class BitIndexDate extends BitIndex<Date, BitFieldDateNotNull, BitIndexDate> {

    public BitIndexDate(final TableColumn tableColumn, SortedMap<Date, Collection<Integer>> valueIndex, List<Integer> nullValues) {
        super(tableColumn, valueIndex, nullValues, new BitArrayDateNotNull(tableColumn));
    }

    @Override
    protected BitIndexDate createInstance(final TableColumn tableColumn, SortedMap<Date, Collection<Integer>> valueIndex, List<Integer> nullValues) {
        return new BitIndexDate(tableColumn, valueIndex, nullValues);
    }

    protected BitIndexDate(BitIndexDate original, TableColumn tableColumn, Collection<Date> keys, boolean nullValues) {
        super(original, tableColumn, keys, nullValues);
    }

    @Override
    protected BitIndexDate createSubIndex(BitIndexDate original, TableColumn tableColumn, Collection<Date> keys, boolean nullValues) {
        return new BitIndexDate(original, tableColumn, keys, nullValues);
    }

}
