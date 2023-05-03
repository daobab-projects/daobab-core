package io.daobab.target.buffer.nonheap.access.index;

import io.daobab.model.TableColumn;
import io.daobab.target.buffer.nonheap.access.array.BitArrayIntegerNotNull;
import io.daobab.target.buffer.nonheap.access.field.BitFieldIntegerNotNull;

import java.util.Collection;
import java.util.List;
import java.util.SortedMap;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class BitIndexInteger extends BitIndex<Integer, BitFieldIntegerNotNull, BitIndexInteger> {

    public BitIndexInteger(final TableColumn tableColumn, SortedMap<Integer, Collection<Integer>> valueIndex, List<Integer> nullValues) {
        super(tableColumn, valueIndex, nullValues, new BitArrayIntegerNotNull(tableColumn));
    }

    @Override
    protected BitIndexInteger createInstance(final TableColumn tableColumn, SortedMap<Integer, Collection<Integer>> valueIndex, List<Integer> nullValues) {
        return new BitIndexInteger(tableColumn, valueIndex, nullValues);
    }

    protected BitIndexInteger(BitIndexInteger original, TableColumn tableColumn, Collection<Integer> keys, boolean nullValues) {
        super(original, tableColumn, keys, nullValues);
    }

    @Override
    protected BitIndexInteger createSubIndex(BitIndexInteger original, TableColumn tableColumn, Collection<Integer> keys, boolean nullValues) {
        return new BitIndexInteger(original, tableColumn, keys, nullValues);
    }


}
