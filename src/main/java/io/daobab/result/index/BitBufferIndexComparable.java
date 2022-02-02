package io.daobab.result.index;

import io.daobab.model.Column;
import io.daobab.model.EntityRelation;
import io.daobab.target.buffer.bytebyffer.BaseByteBuffer;
import io.daobab.statement.condition.Operator;

import java.util.*;

public class BitBufferIndexComparable<E, F extends Comparable<F>> extends BitBufferIndex<E, F> {


    public BitBufferIndexComparable(Column<?, ?, EntityRelation> indexedColumn, BaseByteBuffer<E> buffer) {
        super(indexedColumn, buffer);
    }

    public Collection<Integer> filter(Operator operator, Object key1) {

        F key = (F) key1;
        NavigableMap<F, Collection<Integer>> subManyMap;
        switch (operator) {
            case IN:
            case EQ: {
                subManyMap = new TreeMap<>();
                Collection<Integer> manyResults = valueIndex.get(key);
                if (manyResults != null && !manyResults.isEmpty()) {
                    subManyMap.put(key, manyResults);
                }
                return toOneList(subManyMap);
            }
            case GT: {
                subManyMap = (valueIndex.isEmpty() || (key.compareTo(valueIndex.lastKey()) > 0)) ? new TreeMap<>() : valueIndex.subMap(key, false, valueIndex.lastKey(), true);
                return toOneList(subManyMap);
            }
            case GTEQ: {
                subManyMap = (valueIndex.isEmpty() || (key.compareTo(valueIndex.lastKey()) > 0)) ? new TreeMap<>() : valueIndex.subMap(key, true, valueIndex.lastKey(), true);
                return toOneList(subManyMap);
            }
            case LT: {
                subManyMap = (valueIndex.isEmpty() || (key.compareTo(valueIndex.lastKey()) < 0)) ? new TreeMap<>() : valueIndex.subMap(valueIndex.firstKey(), true, key, false);
                return toOneList(subManyMap);
            }
            case LTEQ: {
                subManyMap = (valueIndex.isEmpty() || (key.compareTo(valueIndex.lastKey()) < 0)) ? new TreeMap<>() : valueIndex.subMap(valueIndex.firstKey(), true, key, true);
                return toOneList(subManyMap);
            }
            case IS_NULL: {
                return new ArrayList<>(nullValues);
            }
            case NOT_NULL: {
                return toOneList(valueIndex);
            }
            default:
                return new LinkedList<>();
        }
    }

}
