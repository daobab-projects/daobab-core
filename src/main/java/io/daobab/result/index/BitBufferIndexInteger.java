package io.daobab.result.index;

import io.daobab.model.Column;
import io.daobab.model.EntityRelation;
import io.daobab.result.BaseByteBuffer;
import io.daobab.statement.condition.Operator;

import java.util.*;

public class BitBufferIndexInteger<E> extends BitBufferIndex<E, Integer> {


    public BitBufferIndexInteger(Column<?, Integer, EntityRelation> indexedColumn, BaseByteBuffer<E> buffer) {
        super(indexedColumn, buffer);
    }

    public Collection<Integer> filter(Operator operator, Object key1) {

        Integer key = (Integer) key1;
        NavigableMap<Integer, Collection<Integer>> subManyMap;
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
                subManyMap = (valueIndex.isEmpty() || key > valueIndex.lastKey()) ? new TreeMap<>() : valueIndex.subMap(key, false, valueIndex.lastKey(), true);
                Collection<Integer> rrr = toOneList(subManyMap);
                return rrr;
            }
            case GTEQ: {
                subManyMap = (valueIndex.isEmpty() || key > valueIndex.lastKey()) ? new TreeMap<>() : valueIndex.subMap(key, true, valueIndex.lastKey(), true);
                return toOneList(subManyMap);
            }
            case LT: {
                subManyMap = (valueIndex.isEmpty() || key < valueIndex.firstKey()) ? new TreeMap<>() : valueIndex.subMap(valueIndex.firstKey(), true, key, false);
                Collection<Integer> tst = toOneList(subManyMap);
                return tst;
            }
            case LTEQ: {
                subManyMap = (valueIndex.isEmpty() || key < valueIndex.firstKey()) ? new TreeMap<>() : valueIndex.subMap(valueIndex.firstKey(), true, key, true);
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
