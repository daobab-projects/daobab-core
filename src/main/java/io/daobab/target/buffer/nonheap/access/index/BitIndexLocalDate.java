package io.daobab.target.buffer.nonheap.access.index;

import io.daobab.model.TableColumn;
import io.daobab.target.buffer.nonheap.access.array.BitArrayLocalDateNotNull;
import io.daobab.target.buffer.nonheap.access.field.BitFieldLocalDateNotNull;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class BitIndexLocalDate extends BitIndex<LocalDate, BitFieldLocalDateNotNull, BitIndexLocalDate> {

    public BitIndexLocalDate(final TableColumn tableColumn, SortedMap<LocalDate, Collection<Integer>> valueIndex, List<Integer> nullValues) {
        super(tableColumn, valueIndex, nullValues, new BitArrayLocalDateNotNull(tableColumn));
    }

    protected BitIndexLocalDate(BitIndexLocalDate original, TableColumn tableColumn, Collection<LocalDate> keys, boolean nullValues) {
        super(original, tableColumn, keys, nullValues);
    }

    @Override
    protected BitIndexLocalDate createInstance(final TableColumn tableColumn, SortedMap<LocalDate, Collection<Integer>> valueIndex, List<Integer> nullValues) {
        return new BitIndexLocalDate(tableColumn, valueIndex, nullValues);
    }

    @Override
    protected BitIndexLocalDate createSubIndex(BitIndexLocalDate original, TableColumn tableColumn, Collection<LocalDate> keys, boolean nullValues) {
        return new BitIndexLocalDate(original, tableColumn, keys, nullValues);
    }

}
