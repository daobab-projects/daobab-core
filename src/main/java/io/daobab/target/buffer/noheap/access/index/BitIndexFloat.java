package io.daobab.target.buffer.noheap.access.index;

import io.daobab.model.TableColumn;
import io.daobab.target.buffer.noheap.access.array.BitArrayFloatNotNull;
import io.daobab.target.buffer.noheap.access.field.BitFieldFloatNotNull;

import java.util.Collection;
import java.util.List;
import java.util.SortedMap;

public class BitIndexFloat extends AbstractBitIndex<Float, BitFieldFloatNotNull, BitIndexFloat> {

    public BitIndexFloat(final TableColumn tableColumn, SortedMap<Float, Collection<Integer>> valueIndex, List<Integer> nullValues) {
        super(tableColumn, valueIndex, nullValues, new BitArrayFloatNotNull(tableColumn));
    }

    @Override
    protected BitIndexFloat createInstance(final TableColumn tableColumn, SortedMap<Float, Collection<Integer>> valueIndex, List<Integer> nullValues) {
        return new BitIndexFloat(tableColumn, valueIndex, nullValues);
    }

}
