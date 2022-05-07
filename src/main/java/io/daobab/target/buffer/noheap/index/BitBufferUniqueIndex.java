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
public abstract class BitBufferUniqueIndex<E, F> implements BitBufferIndexBase<F> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final Column<?, ?, EntityRelation> indexedColumn;
    protected TreeMap<F, Integer> valueIndex = new TreeMap<>();
    protected List<Integer> nullValues = new ArrayList<>();
    private int indexedColumnPosition = 0;
    private int indexedColumnOrder = 0;
    private boolean worthless = false;

    private double efficiency = 0;

    //    //Invoked internally. Initialisation is redundand
    protected BitBufferUniqueIndex(Column<?, ?, EntityRelation> indexedColumn, Map<F, Integer> fieldEntitiesMap, List<Integer> nullValuesEntities) {
        this.indexedColumn = indexedColumn;
        this.valueIndex.putAll(fieldEntitiesMap);
    }

    protected BitBufferUniqueIndex(Column<?, ?, EntityRelation> indexedColumn, NoHeapBuffer<E> buffer) {
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
        Set<Integer> results = new HashSet<>();

        if (Operator.IN.equals(operator)) {
            for (F key : keys) {
                results.addAll(filter(Operator.EQ, key));
            }
        } else if (Operator.NOT_IN.equals(operator)) {
            for (F key : keys) {
                results.addAll(filterNegative(Operator.EQ, key));
            }
        }

        return results;
    }

    public Collection<Integer> filterNegative(Operator operator, Object key1) {
        F key = (F) key1;

        switch (operator) {
            case IN:
            case EQ: {
                NavigableMap<F, Integer> subManyMap = new TreeMap<>();
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

    protected Collection<Integer> toOneList(Map<F, Integer> subManyMap, List<Integer> nullValuesAsPK) {

        int joinedSizes = nullValuesAsPK.size();
        if (joinedSizes == 0) {
            return subManyMap.values();
        }
        int position = 0;

        if (subManyMap != null) {
            joinedSizes = joinedSizes + subManyMap.size();
        }

        List<Integer> rv = new ArrayList<>(joinedSizes);
        for (Integer np : nullValuesAsPK) {
            rv.add(position, np);
            position++;
        }

        if (subManyMap != null) {
            for (Map.Entry<F, Integer> entry : subManyMap.entrySet()) {
                rv.add(position++, entry.getValue());
            }
        }
        return rv;
    }

    public long count() {
        return valueIndex.size();
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
        valueIndex.putIfAbsent(value, pointer);
    }

    public boolean removeValue(F value, int pointer) {
        if (value == null) {
            return nullValues.remove(pointer) != null;
        } else {
            return valueIndex.remove(pointer) != null;
        }
    }

}
