package io.daobab.target.buffer.nonheap.access.index;

import io.daobab.model.TableColumn;
import io.daobab.result.predicate.MatchLike;
import io.daobab.statement.condition.Operator;
import io.daobab.target.buffer.nonheap.access.array.BitArrayStringNotNull;
import io.daobab.target.buffer.nonheap.access.field.BitFieldStringNotNull;
import io.daobab.target.buffer.nonheap.access.field.SizeOnlyTableColumn;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
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


    public Integer[] filter(Operator operator, String key) {

        switch (operator) {

            case LIKE: {
                if (key == null) {
                    return new Integer[0];
                }
                MatchLike matchLike = new MatchLike(key);
                return getKeys().stream().filter(matchLike).map(this::get).flatMap(Arrays::stream).toArray(Integer[]::new);
            }

            case NOT_LIKE: {
                if (key == null) {
                    return new Integer[0];
                }
                MatchLike matchLike = new MatchLike(key);
                return getKeys().stream().filter(k -> !matchLike.test(k)).map(this::get).flatMap(Arrays::stream).toArray(Integer[]::new);
            }

            default:
                return super.filter(operator, key);
        }
    }
}
