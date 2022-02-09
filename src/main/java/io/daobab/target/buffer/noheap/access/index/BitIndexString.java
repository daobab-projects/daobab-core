package io.daobab.target.buffer.noheap.access.index;

import io.daobab.model.TableColumn;
import io.daobab.target.buffer.noheap.access.array.BitArrayStringNotNull;
import io.daobab.target.buffer.noheap.access.field.BitFieldStringNotNull;
import io.daobab.target.buffer.noheap.access.field.SizeOnlyTableColumn;

import java.util.Collection;
import java.util.List;
import java.util.SortedMap;

public class BitIndexString extends AbstractBitIndex<String, BitFieldStringNotNull, BitIndexString> {

    private final int length;


    public BitIndexString(int length, SortedMap<String, Collection<Integer>> valueIndex, List<Integer> nullValues) {
        super(null, valueIndex, nullValues, new BitArrayStringNotNull(new SizeOnlyTableColumn(length)));
        this.length = length;
    }

    public BitIndexString(final TableColumn tableColumn, SortedMap<String, Collection<Integer>> valueIndex, List<Integer> nullValues) {
        super(tableColumn, valueIndex, nullValues, new BitArrayStringNotNull(tableColumn));
        this.length = tableColumn.getSize();
    }

    @Override
    protected BitIndexString createInstance(final TableColumn tableColumn, SortedMap<String, Collection<Integer>> valueIndex, List<Integer> nullValues) {
        return new BitIndexString(tableColumn, valueIndex, nullValues);
    }

}
