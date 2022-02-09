package io.daobab.target.buffer.noheap.access.index;

import io.daobab.model.TableColumn;
import io.daobab.target.buffer.noheap.access.array.BitArrayDateNotNull;
import io.daobab.target.buffer.noheap.access.field.BitFieldDateNotNull;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;

public class BitIndexDate extends AbstractBitIndex<Date, BitFieldDateNotNull, BitIndexDate> {

    public BitIndexDate(final TableColumn tableColumn, SortedMap<Date, Collection<Integer>> valueIndex, List<Integer> nullValues) {
        super(tableColumn, valueIndex, nullValues, new BitArrayDateNotNull(tableColumn));
    }

    @Override
    protected BitIndexDate createInstance(final TableColumn tableColumn, SortedMap<Date, Collection<Integer>> valueIndex, List<Integer> nullValues) {
        return new BitIndexDate(tableColumn, valueIndex, nullValues);
    }
}
