package io.daobab.target.buffer.noheap.access.index;

import io.daobab.model.TableColumn;
import io.daobab.target.buffer.noheap.access.array.BitArrayTimestampNotNull;
import io.daobab.target.buffer.noheap.access.field.BitFieldTimestampNotNull;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;

public class BitIndexTimestamp extends AbstractBitIndex<Timestamp, BitFieldTimestampNotNull, BitIndexTimestamp> {

    public BitIndexTimestamp(TableColumn tableColumn, SortedMap<Timestamp, Collection<Integer>> valueIndex, List<Integer> nullValues) {
        super(tableColumn, valueIndex, nullValues, new BitArrayTimestampNotNull(tableColumn));
    }

    @Override
    protected BitIndexTimestamp createInstance(TableColumn tableColumn, SortedMap<Timestamp, Collection<Integer>> valueIndex, List<Integer> nullValues) {
        return new BitIndexTimestamp(tableColumn, valueIndex, nullValues);
    }

}
