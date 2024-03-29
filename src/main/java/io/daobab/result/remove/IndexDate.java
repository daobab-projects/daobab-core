package io.daobab.result.remove;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.RelatedTo;
import io.daobab.result.EntitiesBufferIndexed;
import io.daobab.result.FakePkEntity;
import io.daobab.statement.condition.Operator;

import java.util.*;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class IndexDate<E extends Entity, F extends Date> extends IndexNumber<E, Long> {

    private IndexDate(Column<E, ?, RelatedTo> indexedColumn, Map<Long, List<E>> oneToManySubMap, List<E> nullValuesAsPK) {
        super(indexedColumn, oneToManySubMap, nullValuesAsPK);
    }

    public IndexDate(Column<E, ?, RelatedTo> indexedColumn, EntitiesBufferIndexed<E> buffer) {
        super(indexedColumn, buffer);
    }

    @Override
    protected Object getColumnValue(FakePkEntity entity) {
        return getIndexedColumn().getValue((RelatedTo) entity.getEntity());
    }

    @Override
    public List<E> filter(Operator operator, Object key1) {
        Long key = ((F) key1).getTime();
        return super.filter(operator, key);
    }

    @Override
    protected Index<E, Long> empty() {
        return new IndexDate<>(getIndexedColumn(), new TreeMap<>(), nullValuesEntities);
    }

    @Override
    protected Index<E, Long> newInstance(Column<E, ?, RelatedTo> indexedColumn, NavigableMap<Long, List<E>> oneToManyMap, List<E> nullValuesAsPK) {
        return new IndexDate(indexedColumn, oneToManyMap, nullValuesAsPK);
    }
}
