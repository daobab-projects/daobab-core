package io.daobab.target.buffer.noheap.access.index;

import io.daobab.model.TableColumn;
import io.daobab.target.buffer.noheap.access.array.BitArrayStringNotNull;
import io.daobab.target.buffer.noheap.access.field.BitFieldStringNotNull;
import io.daobab.target.buffer.noheap.access.field.SizeOnlyTableColumn;

import java.util.Collection;
import java.util.List;
import java.util.SortedMap;

public class BitIndexString extends BitIndex<String, BitFieldStringNotNull, BitIndexString> {

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


    protected BitIndexString(BitIndexString original, TableColumn tableColumn, Collection<String> keys, boolean nullValues) {
        super(original, tableColumn, keys, nullValues);
        this.length = original.length;
    }

    @Override
    protected BitIndexString createSubIndex(BitIndexString original, TableColumn tableColumn, Collection<String> keys, boolean nullValues) {
        return new BitIndexString(original, tableColumn, keys, nullValues);
    }

}
