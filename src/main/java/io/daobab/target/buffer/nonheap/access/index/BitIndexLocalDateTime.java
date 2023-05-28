package io.daobab.target.buffer.nonheap.access.index;

import io.daobab.model.TableColumn;
import io.daobab.target.buffer.nonheap.access.array.BitArrayLocalDateTimeNotNull;
import io.daobab.target.buffer.nonheap.access.field.BitFieldLocalDateTimeNotNull;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class BitIndexLocalDateTime extends BitIndex<LocalDateTime, BitFieldLocalDateTimeNotNull, BitIndexLocalDateTime> {

    public BitIndexLocalDateTime(final TableColumn tableColumn, SortedMap<LocalDateTime, Collection<Integer>> valueIndex, List<Integer> nullValues) {
        super(tableColumn, valueIndex, nullValues, new BitArrayLocalDateTimeNotNull(tableColumn));
    }

    protected BitIndexLocalDateTime(BitIndexLocalDateTime original, TableColumn tableColumn, Collection<LocalDateTime> keys, boolean nullValues) {
        super(original, tableColumn, keys, nullValues);
    }

    @Override
    protected BitIndexLocalDateTime createInstance(final TableColumn tableColumn, SortedMap<LocalDateTime, Collection<Integer>> valueIndex, List<Integer> nullValues) {
        return new BitIndexLocalDateTime(tableColumn, valueIndex, nullValues);
    }

    @Override
    protected BitIndexLocalDateTime createSubIndex(BitIndexLocalDateTime original, TableColumn tableColumn, Collection<LocalDateTime> keys, boolean nullValues) {
        return new BitIndexLocalDateTime(original, tableColumn, keys, nullValues);
    }

}
