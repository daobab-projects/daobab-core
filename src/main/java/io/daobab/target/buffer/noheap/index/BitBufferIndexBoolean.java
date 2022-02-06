package io.daobab.target.buffer.noheap.index;

import io.daobab.model.Column;
import io.daobab.model.EntityRelation;
import io.daobab.statement.condition.Operator;
import io.daobab.target.buffer.noheap.NoHeapBuffer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NavigableMap;
import java.util.TreeMap;

public class BitBufferIndexBoolean<E> extends BitBufferIndex<E, Boolean> {


    @SuppressWarnings("rawtypes")
    public BitBufferIndexBoolean(Column<?, ?, EntityRelation> indexedColumn, NoHeapBuffer<E> buffer) {
        super(indexedColumn, buffer);
    }

    public Collection<Integer> filter(Operator operator, Object key1) {
        Boolean key = (Boolean) key1;
        NavigableMap<Boolean, Collection<Integer>> subManyMap;
        switch (operator) {
            case EQ: {
                subManyMap = new TreeMap<>();

                Collection<Integer> manyResults = valueIndex.get(key);
                if (manyResults != null && !manyResults.isEmpty()) {
                    subManyMap.put(key, manyResults);
                }
                return toOneList(subManyMap);
            }

            case IS_NULL: {
                return nullValues;
            }
            case NOT_NULL: {
                return toOneList(valueIndex);
            }
            default:
                return new ArrayList<>();
        }
    }

}
