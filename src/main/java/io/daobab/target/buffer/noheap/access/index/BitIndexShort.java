package io.daobab.target.buffer.noheap.access.index;

import io.daobab.model.TableColumn;
import io.daobab.target.buffer.noheap.access.array.BitArrayShortNotNull;
import io.daobab.target.buffer.noheap.access.field.BitFieldShortNotNull;

import java.util.Collection;
import java.util.List;
import java.util.SortedMap;

public class BitIndexShort extends BitIndex<Short, BitFieldShortNotNull, BitIndexShort> {

    public BitIndexShort(final TableColumn tableColumn, SortedMap<Short, Collection<Integer>> valueIndex, List<Integer> nullValues) {
        super(tableColumn, valueIndex, nullValues, new BitArrayShortNotNull(tableColumn));
    }

    @Override
    protected BitIndexShort createInstance(final TableColumn tableColumn, SortedMap<Short, Collection<Integer>> valueIndex, List<Integer> nullValues) {
        return new BitIndexShort(tableColumn, valueIndex, nullValues);
    }

    protected BitIndexShort(BitIndexShort original, TableColumn tableColumn, Collection<Short> keys, boolean nullValues) {
        super(original, tableColumn, keys, nullValues);
    }

    @Override
    protected BitIndexShort createSubIndex(BitIndexShort original, TableColumn tableColumn, Collection<Short> keys, boolean nullValues) {
        return new BitIndexShort(original, tableColumn, keys, nullValues);
    }

}
