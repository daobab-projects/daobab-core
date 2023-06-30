package io.daobab.result.remove;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.RelatedTo;
import io.daobab.result.EntitiesBufferIndexed;
import io.daobab.result.FakePkEntity;
import io.daobab.statement.condition.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class Index<E extends Entity, F> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final Column<E, ?, RelatedTo> indexedColumn;

    protected List<E> nullValuesEntities = new ArrayList<>();
    protected TreeMap<F, List<E>> fieldEntitiesMap = new TreeMap<>();

    protected List<Number> nullValuesPk = new ArrayList<>();
    protected TreeMap<F, List<Number>> fieldPkMap = new TreeMap<>();
    protected F firstKeyInMap;
    protected F lastKeyInMap;
    private boolean worthless = false;
    private double efficiency = 0;

    //Invoked internally. Initialisation is redundand
    protected Index(Column<E, ?, RelatedTo> indexedColumn, Map<F, List<E>> fieldEntitiesMap, List<E> nullValuesEntities) {
        this.indexedColumn = indexedColumn;
        this.fieldEntitiesMap.putAll(fieldEntitiesMap);
        this.nullValuesEntities = nullValuesEntities;
        if (!this.fieldEntitiesMap.isEmpty()) {
            this.firstKeyInMap = this.fieldEntitiesMap.firstKey();
            this.lastKeyInMap = this.fieldEntitiesMap.lastKey();
        }
    }


    public Index(Column<E, ?, RelatedTo> indexedColumn, EntitiesBufferIndexed<E> buffer) {
        this.indexedColumn = indexedColumn;
        init(buffer);
        double indexSize = fieldEntitiesMap.size();
        double entitiesSize = buffer.getKeys().size();
        efficiency = (indexSize / entitiesSize) * 100;
        if (!fieldEntitiesMap.isEmpty()) {
            this.firstKeyInMap = fieldEntitiesMap.firstKey();
            this.lastKeyInMap = fieldEntitiesMap.lastKey();
        }

        if (indexSize == 1) {
            worthless = true;
        }
        log.info("Index created for column {}, Total elements: {} indexed size: {}. Index considered as {}", indexedColumn.getEntityName() + "." + indexedColumn.getColumnName(), buffer.size(), indexSize, worthless ? "worthless" : "useful");
    }

    protected <F> F getColumnValue(FakePkEntity<Number, E> entity) {
        return (F) indexedColumn.getValue((RelatedTo) entity.getEntity());
    }

    private void init(EntitiesBufferIndexed<E> elements) {
//        if (elements == null || getIndexedColumn() == null) return;

        for (FakePkEntity<Number, E> fakePkEntity : elements.getValues()) {
            F columnvalue = getColumnValue(fakePkEntity);
            if (columnvalue == null) {
                nullValuesEntities.add(fakePkEntity.getEntity());
                nullValuesPk.add(fakePkEntity.getPk());
                continue;
            }

            List<E> valueRelatedEntities = fieldEntitiesMap.get(columnvalue);
            if (valueRelatedEntities == null) {
                List<E> entities = new ArrayList<>();
                entities.add(fakePkEntity.getEntity());
                fieldEntitiesMap.put(columnvalue, entities);
                List<Number> pkList = new ArrayList<>();
                pkList.add(fakePkEntity.getPk());
                fieldPkMap.put(columnvalue, pkList);
            } else {
                valueRelatedEntities.add(fakePkEntity.getEntity());
                fieldPkMap.get(columnvalue).add(fakePkEntity.getPk());
            }
        }

    }

    public List<E> filter(Operator operator, F... keys) {
        List<E> list = new ArrayList<>();

        if (Operator.IN.equals(operator)) {
            for (F key : keys) {
                list.addAll(filter(Operator.EQ, key));
            }
        } else if (Operator.NOT_IN.equals(operator)) {
            for (F key : keys) {
                list.addAll(filterNegative(Operator.EQ, key));
            }
        }
        return list;
    }

    public List<Number> filterPk(Operator operator, F... keys) {
        List<Number> list = new ArrayList<>();

        if (Operator.IN.equals(operator)) {
            for (F key : keys) {
                list.addAll(filterPk(Operator.EQ, key));
            }
        } else if (Operator.NOT_IN.equals(operator)) {
            for (F key : keys) {
                list.addAll(filterPkNegative(Operator.EQ, key));
            }
        }
//        merge(list);
        return list;
    }

    public abstract List<E> filter(Operator operator, Object key);

    public abstract List<Number> filterPk(Operator operator, Object key);


    public List<E> filterNegative(Operator operator, Object key1) {
        F key = (F) key1;
        NavigableMap<F, List<E>> subManyMap;
        switch (operator) {
            case IN:
            case EQ: {
                subManyMap = fieldEntitiesMap;
                subManyMap.remove(key);
                return toPkList(subManyMap, nullValuesEntities);
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
                return null;
        }
    }

    public List<Number> filterPkNegative(Operator operator, Object key1) {
        F key = (F) key1;
        NavigableMap<F, List<Number>> subManyMap;
        switch (operator) {
            case IN:
            case EQ: {
                subManyMap = fieldPkMap;
                subManyMap.remove(key);
                return toPkList2(subManyMap, nullValuesPk);
            }
            case GT: {
                return filterPk(Operator.LTEQ, key);
            }
            case GTEQ: {
                return filterPk(Operator.LT, key);
            }
            case LT: {
                return filterPk(Operator.GTEQ, key);
            }
            case LTEQ: {
                return filterPk(Operator.GT, key);
            }
            case IS_NULL: {
                return filterPk(Operator.NOT_NULL, key);
            }
            case NOT_NULL: {
                return filterPk(Operator.IS_NULL, key);
            }
            default:
                return null;
        }
    }


    protected abstract Index<E, F> empty();

    public List<E> toPkList() {
        return toPkList(fieldEntitiesMap, nullValuesEntities);
    }

    protected List<Number> toPkList2(Map<F, List<Number>> subManyMap, List<Number> nullValuesAsPK) {
        List<Number> tempCollection = new ArrayList<>();

        tempCollection.addAll(nullValuesAsPK);
        if (subManyMap != null) {
            for (Map.Entry<F, List<Number>> entry : subManyMap.entrySet()) {
                tempCollection.addAll(entry.getValue());
            }
        }

        return tempCollection;
    }

    protected List<E> toPkList(Map<F, List<E>> subManyMap, List<E> nullValuesAsPK) {
        List<E> tempCollection = new ArrayList<>();

        tempCollection.addAll(nullValuesAsPK);
        if (subManyMap != null) {
            for (Map.Entry<F, List<E>> entry : subManyMap.entrySet()) {
                tempCollection.addAll(entry.getValue());
            }
        }

        return tempCollection;
    }

//
//    public Index<K, E, F> merge(List<Index<K, E, F>> another) {
//        if (another==null) return null;
//
//        SortedSet<K> nullValuesAsPK = new TreeSet<>();
//        TreeMap<F, SortedSet<K>> oneToManyMap = new TreeMap<>();
//        TreeMap<F, K> oneToOneMap = new TreeMap<>();
//        Column<E, F, EntityRelation> indexedColumn=null;
//
//        for (Index<K, E, F> a:another){
////            indexedColumn=(Column<E, F, EntityRelation>)a.indexedColumn;
//            oneToManyMap.putAll(a.oneToManyMap);
//            oneToOneMap.putAll(a.oneToOneMap);
//            nullValuesAsPK.addAll(a.nullValuesAsPK);
//        }
//
//        return newInstance(indexedColumn,oneToManyMap,oneToOneMap,nullValuesAsPK);
//    }

    public long count() {
        long count = 0;
        for (List<E> pkset : fieldEntitiesMap.values()) {
            count = count + pkset.size();
        }
        return count;
    }

//    public boolean hasPrimaryKey(K pk) {
//        if (resultSet == null) {
//            toPkList();
//        }
//        return resultSet.contains(pk);
//        //long pklong=pk.longValue();
////        for (Map.Entry<F,SortedSet<O>> entry:valueMap.entrySet()){
////
////            if (entry.getValue().contains(pk)){
////                return true;
////            }
//////            boolean highersLeft=false;
//////            for (Iterator<O> oi=entry.getValue().iterator();oi.hasNext()&&!highersLeft;){
//////
//////                long val=oi.next().longValue();
//////                if (val<pklong){
//////                    oi.remove();
//////                    continue;
//////                }
//////                if (val==pklong){
//////                    oi.remove();
//////                    return true;
//////                }
//////                highersLeft=true;
//////            }
////        }
////        return false;
//    }

//    //Searches for the largest previous timestamp less than or equal to the time stamp we are searchign for
//    private int binarySearch(F key) {
//        int low = 0;
//        int high = valueMap.keySet().size()-1;
//
//        Number number=key;
//        List<Number> keySet=new LinkedList<>(valueMap.keySet());
//        while (low<high -1) {
//            int mid = low + (high-low)/2;
//            if (keySet.get(mid).longValue() < number.longValue()) low = mid;
//            else high = mid;
//        }
//        if (keySet.get(high).longValue()<=number.longValue()) return high;
//        else return low;
//    }

    protected abstract Index<E, F> newInstance(Column<E, ?, RelatedTo> indexedColumn, NavigableMap<F, List<E>> oneToManyMap, List<E> nullValuesAsPK);

    public Column<E, ?, RelatedTo> getIndexedColumn() {
        return indexedColumn;
    }


    public boolean isWorthless() {
        return worthless;
    }

}
