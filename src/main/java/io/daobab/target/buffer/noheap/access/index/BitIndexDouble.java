package io.daobab.target.buffer.noheap.access.index;

import io.daobab.model.TableColumn;
import io.daobab.target.buffer.noheap.access.array.BitArrayDoubleNotNull;
import io.daobab.target.buffer.noheap.access.field.BitFieldDoubleNotNull;

import java.util.Collection;
import java.util.List;
import java.util.SortedMap;

public class BitIndexDouble extends AbstractBitIndex<Double, BitFieldDoubleNotNull, BitIndexDouble> {

    public BitIndexDouble(final TableColumn tableColumn, SortedMap<Double, Collection<Integer>> valueIndex, List<Integer> nullValues) {
        super(tableColumn, valueIndex, nullValues, new BitArrayDoubleNotNull(tableColumn));
    }

    @Override
    protected BitIndexDouble createInstance(final TableColumn tableColumn, SortedMap<Double, Collection<Integer>> valueIndex, List<Integer> nullValues) {
        return new BitIndexDouble(tableColumn, valueIndex, nullValues);
    }

}
