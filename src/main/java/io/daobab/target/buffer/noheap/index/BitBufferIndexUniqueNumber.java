package io.daobab.target.buffer.noheap.index;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;
import io.daobab.statement.condition.Operator;
import io.daobab.target.buffer.noheap.NoHeapBuffer;

import java.util.*;

public class BitBufferIndexUniqueNumber<E extends Entity, F extends Number> extends BitBufferUniqueIndex<E, F> {


    public BitBufferIndexUniqueNumber(Column<E, ?, EntityRelation> indexedColumn, NoHeapBuffer<E> buffer) {
        super(indexedColumn, buffer);
    }

    public Collection<Integer> filter(Operator operator, Object key1) {

        F key = (F) key1;
        NavigableMap<F, Integer> subManyMap;
        switch (operator) {
            case IN:
            case EQ: {
                return Collections.singletonList(valueIndex.get(key));
            }
            case GT: {
                subManyMap = (valueIndex.isEmpty() || key.doubleValue() > valueIndex.lastKey().doubleValue()) ? new TreeMap<>() : valueIndex.subMap(key, false, valueIndex.lastKey(), true);
                return subManyMap.values();
            }
            case GTEQ: {
                subManyMap = (valueIndex.isEmpty() || key.doubleValue() > valueIndex.lastKey().doubleValue()) ? new TreeMap<>() : valueIndex.subMap(key, true, valueIndex.lastKey(), true);
                return subManyMap.values();
            }
            case LT: {
                subManyMap = (valueIndex.isEmpty() || key.doubleValue() < valueIndex.firstKey().doubleValue()) ? new TreeMap<>() : valueIndex.subMap(valueIndex.firstKey(), true, key, false);
                return subManyMap.values();
            }
            case LTEQ: {
                subManyMap = (valueIndex.isEmpty() || key.doubleValue() < valueIndex.firstKey().doubleValue()) ? new TreeMap<>() : valueIndex.subMap(valueIndex.firstKey(), true, key, true);
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
