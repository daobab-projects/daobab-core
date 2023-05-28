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
public class IndexNumber<E extends Entity, F extends Number> extends Index<E, F> {


    protected IndexNumber(Column<E, ?, RelatedTo> indexedColumn, Map<F, List<E>> oneToManySubMap, List<E> nullValuesAsPK) {
        super(indexedColumn, oneToManySubMap, nullValuesAsPK);
    }

    public IndexNumber(Column<E, ?, RelatedTo> indexedColumn, EntitiesBufferIndexed<E> buffer) {
        super(indexedColumn, buffer);
    }

    public List<E> filter(Operator operator, Object key1) {
        F key = (F) key1;
        NavigableMap<F, List<E>> subManyMap;
        switch (operator) {
            case IN:
            case EQ: {
                subManyMap = new TreeMap<>();
                List<E> manyResults = fieldEntitiesMap.get(key);
                if (manyResults != null && !manyResults.isEmpty()) {
                    subManyMap.put(key, manyResults);
                }
                return toPkList(subManyMap, new LinkedList<>());
            }
            case GT: {
                subManyMap = (lastKeyInMap == null || fieldEntitiesMap.isEmpty() || key.longValue() > lastKeyInMap.longValue()) ? new TreeMap<>() : fieldEntitiesMap.subMap(key, false, lastKeyInMap, true);
                return toPkList(subManyMap, new LinkedList<>());
            }
            case GTEQ: {
                subManyMap = (lastKeyInMap == null || fieldEntitiesMap.isEmpty() || key.longValue() > lastKeyInMap.longValue()) ? new TreeMap<>() : fieldEntitiesMap.subMap(key, true, lastKeyInMap, true);
                return toPkList(subManyMap, new LinkedList<>());
            }
            case LT: {
                subManyMap = (firstKeyInMap == null || fieldEntitiesMap.isEmpty() || key.longValue() < firstKeyInMap.longValue()) ? new TreeMap<>() : fieldEntitiesMap.subMap(firstKeyInMap, true, key, false);
                return toPkList(subManyMap, new LinkedList<>());
            }
            case LTEQ: {
                subManyMap = (firstKeyInMap == null || fieldEntitiesMap.isEmpty() || key.longValue() < firstKeyInMap.longValue()) ? new TreeMap<>() : fieldEntitiesMap.subMap(firstKeyInMap, true, key, true);
                return toPkList(subManyMap, new LinkedList<>());
            }
            case IS_NULL: {
                return toPkList(new TreeMap<>(), nullValuesEntities);
            }
            case NOT_NULL: {
                return toPkList(fieldEntitiesMap, new LinkedList<>());
            }
            default:
                return new ArrayList<>();
        }
    }

    public List<Number> filterPk(Operator operator, Object key1) {
        F key = (F) key1;
        NavigableMap<F, List<Number>> subManyMap;
        switch (operator) {
            case IN:
            case EQ: {
                subManyMap = new TreeMap<>();
                List<Number> manyResults = fieldPkMap.get(key);
                if (manyResults != null && !manyResults.isEmpty()) {
                    subManyMap.put(key, manyResults);
                }
                return toPkList2(subManyMap, new LinkedList<>());
            }
            case GT: {
                subManyMap = (lastKeyInMap == null || fieldPkMap.isEmpty() || key.longValue() > lastKeyInMap.longValue()) ? new TreeMap<>() : fieldPkMap.subMap(key, false, lastKeyInMap, true);
                return toPkList2(subManyMap, new LinkedList<>());
            }
            case GTEQ: {
                subManyMap = (lastKeyInMap == null || fieldPkMap.isEmpty() || key.longValue() > lastKeyInMap.longValue()) ? new TreeMap<>() : fieldPkMap.subMap(key, true, lastKeyInMap, true);
                return toPkList2(subManyMap, new LinkedList<>());
            }
            case LT: {
                subManyMap = (firstKeyInMap == null || fieldPkMap.isEmpty() || key.longValue() < firstKeyInMap.longValue()) ? new TreeMap<>() : fieldPkMap.subMap(firstKeyInMap, true, key, false);
                return toPkList2(subManyMap, new LinkedList<>());
            }
            case LTEQ: {
                subManyMap = (firstKeyInMap == null || fieldPkMap.isEmpty() || key.longValue() < firstKeyInMap.longValue()) ? new TreeMap<>() : fieldPkMap.subMap(firstKeyInMap, true, key, true);
                return toPkList2(subManyMap, new LinkedList<>());
            }
            case IS_NULL: {
                return toPkList2(new TreeMap<>(), nullValuesPk);
            }
            case NOT_NULL: {
                return toPkList2(fieldPkMap, new ArrayList<>());
            }
            default:
                return new ArrayList<>();
        }
    }

    protected Index<E, F> empty() {
        return new IndexNumber<>(getIndexedColumn(), new TreeMap<>(), nullValuesEntities);
    }

    @Override
    protected Index<E, F> newInstance(Column<E, ?, RelatedTo> indexedColumn, NavigableMap<F, List<E>> oneToManyMap, List<E> nullValuesAsPK) {
        return new IndexNumber<>(indexedColumn, oneToManyMap, nullValuesAsPK);
    }
}
