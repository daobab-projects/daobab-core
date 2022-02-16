package io.daobab.target.buffer.noheap.index;

import io.daobab.model.Column;
import io.daobab.model.EntityRelation;
import io.daobab.result.predicate.MatchLike;
import io.daobab.statement.condition.Operator;
import io.daobab.target.buffer.noheap.NoHeapBuffer;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class BitBufferIndexBigDecimal<E> extends BitBufferIndex<E, BigDecimal> {

    public BitBufferIndexBigDecimal(Column<?, ?, EntityRelation> indexedColumn, NoHeapBuffer<E> buffer) {
        super(indexedColumn, buffer);
    }

    public Collection<Integer> filter(Operator operator, Object key1) {
        BigDecimal key = (BigDecimal) key1;
        NavigableMap<BigDecimal, Collection<Integer>> subManyMap;
        switch (operator) {
            case EQ: {
                subManyMap = new TreeMap<>();
                Collection<Integer> manyResults = valueIndex.get(key);
                if (manyResults != null && !manyResults.isEmpty()) {
                    subManyMap.put(key, manyResults);
                }
                return toOneList(subManyMap);
            }
            case LIKE: {
                String keyLike = (String) key1;
                subManyMap = new TreeMap<>();
                if (keyLike == null) {
                    return toOneList(subManyMap);
                }

                MatchLike matchLike = new MatchLike(keyLike);
                for (Map.Entry<BigDecimal, Collection<Integer>> entry : valueIndex.entrySet()) {
                    if (matchLike.test(entry.getKey())) {
                        subManyMap.put(entry.getKey(), entry.getValue());
                    }
                }
                return toOneList(subManyMap);
            }

            case NOT_LIKE: {
                String keyLike = (String) key1;
                subManyMap = new TreeMap<>();
                if (keyLike == null) {
                    return toOneList(subManyMap);
                }

                MatchLike matchLike = new MatchLike(keyLike);
                for (Map.Entry<BigDecimal, Collection<Integer>> entry : valueIndex.entrySet()) {
                    if (!matchLike.test(entry.getKey())) {
                        subManyMap.put(entry.getKey(), entry.getValue());
                    }
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
                return toOneList(valueIndex);
        }
    }


}
