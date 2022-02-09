package io.daobab.target.buffer.noheap.access.index;

import io.daobab.model.TableColumn;
import io.daobab.target.buffer.noheap.access.array.BitArrayIntegerNotNull;
import io.daobab.target.buffer.noheap.access.field.BitFieldIntegerNotNull;

import java.util.Collection;
import java.util.List;
import java.util.SortedMap;

public class BitIndexInteger extends AbstractBitIndex<Integer, BitFieldIntegerNotNull, BitIndexInteger> {

    public BitIndexInteger(final TableColumn tableColumn, SortedMap<Integer, Collection<Integer>> valueIndex, List<Integer> nullValues) {
        super(tableColumn, valueIndex, nullValues, new BitArrayIntegerNotNull(tableColumn));
    }

    @Override
    protected BitIndexInteger createInstance(final TableColumn tableColumn, SortedMap<Integer, Collection<Integer>> valueIndex, List<Integer> nullValues) {
        return new BitIndexInteger(tableColumn, valueIndex, nullValues);
    }

}
