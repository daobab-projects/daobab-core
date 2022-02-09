package io.daobab.target.buffer.noheap.access.index;

import io.daobab.model.TableColumn;
import io.daobab.target.buffer.noheap.access.array.BitArrayLongNotNull;
import io.daobab.target.buffer.noheap.access.field.BitFieldLongNotNull;
import io.daobab.target.buffer.noheap.access.field.EmptyTableColumn;

import java.util.Collection;
import java.util.List;
import java.util.SortedMap;

public class BitIndexLong extends AbstractBitIndex<Long, BitFieldLongNotNull, BitIndexLong> {

    public BitIndexLong(SortedMap<Long, Collection<Integer>> valueIndex, List<Integer> nullValues) {
        this(new EmptyTableColumn(), valueIndex, nullValues);
    }

    public BitIndexLong(final TableColumn tableColumn, SortedMap<Long, Collection<Integer>> valueIndex, List<Integer> nullValues) {
        super(tableColumn, valueIndex, nullValues, new BitArrayLongNotNull(tableColumn));
    }

    @Override
    protected BitIndexLong createInstance(final TableColumn tableColumn, SortedMap<Long, Collection<Integer>> valueIndex, List<Integer> nullValues) {
        return new BitIndexLong(tableColumn, valueIndex, nullValues);
    }

}
