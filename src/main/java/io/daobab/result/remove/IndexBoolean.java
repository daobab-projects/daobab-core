package io.daobab.result.remove;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.RelatedTo;
import io.daobab.result.EntitiesBufferIndexed;
import io.daobab.statement.condition.Operator;

import java.util.*;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class IndexBoolean<E extends Entity, F extends Boolean> extends Index<E, F> {

    @SuppressWarnings("rawtypes")
    private IndexBoolean(Column<E, ?, RelatedTo> indexedColumn, Map<F, List<E>> oneToManySubMap, List<E> nullValuesAsPK) {
        super(indexedColumn, oneToManySubMap, nullValuesAsPK);
    }

    @SuppressWarnings("rawtypes")
    public IndexBoolean(Column<E, ?, RelatedTo> indexedColumn, EntitiesBufferIndexed<E> buffer) {
        super(indexedColumn, buffer);
    }

    public List<E> filter(Operator operator, Object key1) {
        F key = (F) key1;
        NavigableMap<F, List<E>> subManyMap;
        switch (operator) {
            case EQ: {
                subManyMap = new TreeMap<>();

                List<E> manyResults = fieldEntitiesMap.get(key);
                if (manyResults != null && !manyResults.isEmpty()) {
                    subManyMap.put(key, manyResults);
                }
                return toPkList(subManyMap, new LinkedList<>());
            }

            case IS_NULL: {
                return toPkList(new TreeMap<>(), nullValuesEntities);
            }
            case NOT_NULL: {
                return toPkList(fieldEntitiesMap, new LinkedList<>());
            }
            default:
                return null;
        }
    }


    public List<Number> filterPk(Operator operator, Object key1) {
        F key = (F) key1;
        NavigableMap<F, List<Number>> subManyMap;
        switch (operator) {
            case EQ: {
                subManyMap = new TreeMap<>();

                List<Number> manyResults = fieldPkMap.get(key);
                if (manyResults != null && !manyResults.isEmpty()) {
                    subManyMap.put(key, manyResults);
                }
                return toPkList2(subManyMap, new LinkedList<>());
            }

            case IS_NULL: {
                return toPkList2(new TreeMap<>(), nullValuesPk);
            }
            case NOT_NULL: {
                return toPkList2(fieldPkMap, new LinkedList<>());
            }
            default:
                return null;
        }
    }


    protected IndexBoolean<E, F> empty() {
        return new IndexBoolean<>(getIndexedColumn(), new TreeMap<>(), nullValuesEntities);
    }

    @Override
    protected Index<E, F> newInstance(Column<E, ?, RelatedTo> indexedColumn, NavigableMap<F, List<E>> oneToManyMap, List<E> nullValuesAsPK) {
        return new IndexBoolean<>(indexedColumn, oneToManyMap, nullValuesAsPK);
    }
}
