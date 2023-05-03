package io.daobab.target.buffer.nonheap.access.index;

import io.daobab.model.TableColumn;
import io.daobab.target.buffer.nonheap.access.array.BitArrayFloatNotNull;
import io.daobab.target.buffer.nonheap.access.field.BitFieldFloatNotNull;

import java.util.Collection;
import java.util.List;
import java.util.SortedMap;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class BitIndexFloat extends BitIndex<Float, BitFieldFloatNotNull, BitIndexFloat> {

    public BitIndexFloat(final TableColumn tableColumn, SortedMap<Float, Collection<Integer>> valueIndex, List<Integer> nullValues) {
        super(tableColumn, valueIndex, nullValues, new BitArrayFloatNotNull(tableColumn));
    }

    @Override
    protected BitIndexFloat createInstance(final TableColumn tableColumn, SortedMap<Float, Collection<Integer>> valueIndex, List<Integer> nullValues) {
        return new BitIndexFloat(tableColumn, valueIndex, nullValues);
    }

    protected BitIndexFloat(BitIndexFloat original, TableColumn tableColumn, Collection<Float> keys, boolean nullValues) {
        super(original, tableColumn, keys, nullValues);
    }

    @Override
    protected BitIndexFloat createSubIndex(BitIndexFloat original, TableColumn tableColumn, Collection<Float> keys, boolean nullValues) {
        return new BitIndexFloat(original, tableColumn, keys, nullValues);
    }

}
