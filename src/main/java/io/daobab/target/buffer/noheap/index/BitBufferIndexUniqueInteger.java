package io.daobab.target.buffer.noheap.index;

import io.daobab.model.Column;
import io.daobab.model.EntityRelation;
import io.daobab.statement.condition.Operator;
import io.daobab.target.buffer.noheap.NoHeapBuffer;

import java.util.*;

public class BitBufferIndexUniqueInteger<E> extends BitBufferUniqueIndex<E, Integer> {


    public BitBufferIndexUniqueInteger(Column<?, ?, EntityRelation> indexedColumn, NoHeapBuffer<E> buffer) {
        super(indexedColumn, buffer);
    }

    public Collection<Integer> filter(Operator operator, Object key1) {

        Integer key = (Integer) key1;
        NavigableMap<Integer, Integer> subManyMap;
        switch (operator) {
            case IN:
            case EQ: {
                return Collections.singletonList(valueIndex.get(key));
            }
            case GT: {
                subManyMap = (valueIndex.isEmpty() || key > valueIndex.lastKey()) ? new TreeMap<>() : valueIndex.subMap(key, false, valueIndex.lastKey(), true);
                return subManyMap.values();
            }
            case GTEQ: {
                subManyMap = (valueIndex.isEmpty() || key > valueIndex.lastKey()) ? new TreeMap<>() : valueIndex.subMap(key, true, valueIndex.lastKey(), true);
                return subManyMap.values();
            }
            case LT: {
                subManyMap = (valueIndex.isEmpty() || key < valueIndex.firstKey()) ? new TreeMap<>() : valueIndex.subMap(valueIndex.firstKey(), true, key, false);
                return subManyMap.values();
            }
            case LTEQ: {
                subManyMap = (valueIndex.isEmpty() || key < valueIndex.firstKey()) ? new TreeMap<>() : valueIndex.subMap(valueIndex.firstKey(), true, key, true);
                return subManyMap.values();
            }
            case IS_NULL: {
                return nullValues;
            }
            case NOT_NULL: {
                return valueIndex.values();
            }
            default:
                return new ArrayList<>();
        }
    }


}
