package io.daobab.target.buffer.noheap.index;

import io.daobab.model.Column;
import io.daobab.model.EntityRelation;
import io.daobab.statement.condition.Operator;
import io.daobab.target.buffer.noheap.NoHeapBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public abstract class BitBufferIndex<E, F> implements BitBufferIndexBase<F> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final Column<?, ?, EntityRelation> indexedColumn;
    protected TreeMap<F, Collection<Integer>> valueIndex = new TreeMap<>();
    protected List<Integer> nullValues = new ArrayList<>();
    private int indexedColumnPosition = 0;
    private int indexedColumnOrder = 0;
    private boolean worthless = false;

    private double efficiency = 0;

    protected BitBufferIndex(Column<?, ?, EntityRelation> indexedColumn, NoHeapBuffer<E> buffer) {
        this.indexedColumn = indexedColumn;
        this.indexedColumnPosition = buffer.getBufferPositionOfColumn(indexedColumn);
        this.indexedColumnOrder = buffer.getColumnIntoEntityPosition(indexedColumn);
        init(buffer);
        double indexSize = valueIndex.size();
        double entitiesSize = buffer.size();
        efficiency = (indexSize / entitiesSize) * 100;

        if (indexSize == 1) {
            worthless = true;
        }
        log.info("Index created for column {}, Total elements: {} indexed size: {}. Index considered as {}", indexedColumn.getEntityName() + "." + indexedColumn.getColumnName(), buffer.size(), indexSize, worthless ? "worthless" : "useful");
    }

    @SuppressWarnings("unchecked")
    private void init(NoHeapBuffer<E> elements) {
        for (int i = 0; i < elements.size(); i++) {
            F columnvalue = (F) elements.getValueForColumnPosition(i, indexedColumnOrder, indexedColumnPosition);
            addValue(columnvalue, i);
        }
    }

    public Collection<Integer> filter(Operator operator, F... keys) {
        NavigableMap<F, Collection<Integer>> subManyMap = new TreeMap<>();

        if (Operator.IN.equals(operator)) {
            for (F key : keys) {
                subManyMap.put(key, filter(Operator.EQ, key));
            }
        } else if (Operator.NOT_IN.equals(operator)) {
            for (F key : keys) {
                subManyMap.put(key, filterNegative(Operator.EQ, key));
            }
        }

        return toOneList(subManyMap);
    }

    public Collection<Integer> filterNegative(Operator operator, Object key1) {
        F key = (F) key1;
        switch (operator) {
            case IN:
            case EQ: {
                NavigableMap<F, Collection<Integer>> subManyMap = new TreeMap<>();
                subManyMap.putAll(valueIndex);
                subManyMap.remove(key);
                return toOneList(subManyMap, nullValues);
            }
            case GT: {
                return filter(Operator.LTEQ, key);
            }
            case GTEQ: {
                return filter(Operator.LT, key);
            }
            case LT: {
                return filter(Operator.GTEQ, key);
            }
            case LTEQ: {
                return filter(Operator.GT, key);
            }
            case IS_NULL: {
                return filter(Operator.NOT_NULL, key);
            }
            case NOT_NULL: {
                return filter(Operator.IS_NULL, key);
            }
            default:
                return Collections.emptyList();
        }
    }

    protected Collection<Integer> toOneList(Map<F, Collection<Integer>> subManyMap) {
        List<Integer> rv = new ArrayList<>();
        if (subManyMap != null) {
            for (Map.Entry<F, Collection<Integer>> entry : subManyMap.entrySet()) {
                rv.addAll(entry.getValue());
            }
        }
        return rv;
    }

    protected Collection<Integer> toOneList(Map<F, Collection<Integer>> subManyMap, List<Integer> nullValuesAsPK) {

        List<Integer> rv = new ArrayList<>(nullValuesAsPK);

        if (subManyMap != null) {
            for (Map.Entry<F, Collection<Integer>> entry : subManyMap.entrySet()) {
                rv.addAll(entry.getValue());
            }
        }
        return rv;
    }

    public long count() {
        long count = 0;
        for (Collection<Integer> pkset : valueIndex.values()) {
            count = count + pkset.size();
        }
        return count;
    }

    public Column<?, ?, EntityRelation> getIndexedColumn() {
        return indexedColumn;
    }

    public boolean isWorthless() {
        return worthless;
    }

    public void addValue(F value, int pointer) {
        if (value == null) {
            nullValues.add(pointer);
            return;
        }

        Collection<Integer> valueRelatedPositions = valueIndex.get(value);
        if (valueRelatedPositions == null) {
            List<Integer> positions = new ArrayList<>();
            positions.add(pointer);
            valueIndex.put(value, positions);
        } else {
            valueRelatedPositions.add(pointer);
        }
    }

    public boolean removeValue(F value, int pointer) {
        if (value == null) {
            nullValues.remove(pointer);
            return true;
        } else {
            Collection<Integer> valueRelatedPositions = valueIndex.get(value);
            if (valueRelatedPositions != null) {
                valueRelatedPositions.remove(pointer);
                return true;
            }
        }
        return false;
    }

    protected Comparator<Integer> getKeyComparator() {
        return Integer::compareTo;
    }

}
