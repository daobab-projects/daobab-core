package io.daobab.target.buffer.single;

import io.daobab.clone.EntityDuplicator;
import io.daobab.error.BufferedOperationAllowedOnlyForSingleEntityColumns;
import io.daobab.error.DaobabException;
import io.daobab.model.*;
import io.daobab.query.base.Query;
import io.daobab.statement.condition.Limit;
import io.daobab.statement.condition.base.OrderComparatorPlate;
import io.daobab.target.buffer.transaction.OpenedTransactionBufferTarget;
import io.daobab.target.buffer.bytebyffer.PlateBufferIndexed;
import io.daobab.target.buffer.query.*;
import io.daobab.target.protection.AccessProtector;
import io.daobab.target.protection.BasicAccessProtector;
import io.daobab.target.protection.OperationType;
import io.daobab.target.statistic.StatisticCollector;
import io.daobab.target.statistic.StatisticCollectorImpl;
import io.daobab.target.statistic.StatisticCollectorProvider;
import io.daobab.target.statistic.StatisticProvider;
import io.daobab.target.statistic.table.StatisticRecord;
import io.daobab.transaction.Propagation;
import org.slf4j.Logger;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Buffered Plates collection.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class PlateBuffer extends PlateBufferIndexed implements Plates, StatisticCollectorProvider, StatisticProvider {

    private static final long serialVersionUID = 2291798166104201910L;
    private final transient boolean transactionActive = false;

    private transient StatisticCollector statistic;
    private boolean statisticEnabled = false;

    private transient AccessProtector accessProtector = new BasicAccessProtector();

    public PlateBuffer() {}

    public PlateBuffer(List<Plate> entities) {
        super(entities);
    }

    @Override
    public boolean isTransactionActive() {
        return transactionActive;
    }

    @Override
    public boolean isLogQueriesEnabled() {
        return false;
    }

    @Override
    public <T> T aroundTransaction(Supplier<T> t) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E1 extends Entity> E1 readEntity(BufferQueryEntity<E1> query) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.READ);
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        Plates rv = new PlateBuffer(filter(query));
        rv.orderAndLimit(query);
        if (rv.isEmpty()) {
            if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, 0);
            return null;
        }
        if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, 1);
        return (E1) rv.get(0);
    }

    @Override
    public <E extends Entity> Entities<E> readEntityList(BufferQueryEntity<E> query) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.READ);
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        List<Plate> plateList = filter(query);
        List<E> rrv = new ArrayList<>(plateList.size());
        for (int i = 0; i < plateList.size(); i++) {
            rrv.add(i, plateList.get(i).getEntity(query.getEntityClass()));
        }

        EntityList<E> rv = new EntityList<>(rrv, query.getEntityClass());
        rv.orderAndLimit(query);
        if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, rv.size());
        return rv;
    }

    @SuppressWarnings({"unchecked","rawtypes"})
    private <E extends Entity, R extends EntityRelation> PlateBuffer resultPlateList(BufferQueryPlate query) {
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        PlateBuffer matched = new PlateBuffer(filter((Query<E,?,?>) query));
        if (matched.isEmpty()){
            if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, 0);
            return new PlateBuffer();
        }

        List<Plate> results = new ArrayList<>();
        Plates elements = matched.orderAndLimit(query);

        for (Plate element : elements) {
            Plate plate = new Plate(query.getFields());
            for (TableColumn tableColumn : query.getFields()) {
                plate.setValue(tableColumn, tableColumn.getColumn().getValueOf((R) element));
            }
            results.add(plate);
        }
        if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, results.size());
        return new PlateBuffer(results);
    }

    private Plate readPlateFromBuffer(BufferQueryPlate query) {
        PlateBuffer plateBuffer = resultPlateList(query);
        return plateBuffer.isEmpty() ? null : plateBuffer.get(0);
    }

    @SuppressWarnings("unchecked")
    private <E extends Entity, R extends EntityRelation, F> List<F> resultFieldListFromBuffer(BufferQueryField<E, F> query) {
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        PlateBuffer matched = new PlateBuffer(filter(query));
        if (matched.isEmpty()){
            if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, 0);
            return new ArrayList<>();
        }
        Plates elements = matched.orderAndLimit(query);
        Column<E, F, R> firstColumn = query.getFields().get(0).getColumn();
        List<F> results = elements.stream().map(e -> firstColumn.getValueOf((R) e)).collect(Collectors.toList());
        if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, results.size());
        return results;

    }

    @SuppressWarnings({"unchecked","rawtypes"})
    private <E extends Entity, R extends EntityRelation, F> F readFieldFromBuffer(Plates entities, BufferQueryField<E, F> query) {
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        PlateBuffer matched = new PlateBuffer(entities.filter(query));

        if (matched.isEmpty()) {
            if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, 0);
            return null;
        }
        Plate el = matched.orderAndLimit(query).get(0);
        if (el == null) {
            if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, 0);
            return null;
        }

        Column<E, F, R> col = query.getFields().get(0).getColumn();
        F rv = col.getValueOf((R) el);
        if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, 1);
        return rv;
    }

    public Plates orderAndLimit(Query<?,?,?> query) {
        if (query == null) return this;
        PlateBuffer copy = new PlateBuffer(this);
        if (query.getOrderBy() != null) {
            OrderComparatorPlate comparator = new OrderComparatorPlate(query.getOrderBy().toOrderFieldList());
            copy.sort(comparator);
            return copy.limit(query);
        }else{
            return limit(query);
        }
    }

    @Override
    public Plates limit(BufferQueryPlate query) {
        if (query == null) return this;
        if (query.getLimit() != null) {
            Limit limit = query.getLimit();
            if (limit.getOffset() + limit.getLimit() < size()) {
                return new PlateBuffer(subList(limit.getOffset(), limit.getOffset() + limit.getLimit()));
            }else if (limit.getOffset()>0){
                return new PlateBuffer(subList(limit.getOffset(), size()));
            }
        }
        return this;
    }

    private <E extends Entity> Plates limit(Query<E,?,?> query) {
        if (query == null) return this;
        if (query.getLimit() != null) {
            Limit limit = query.getLimit();
            if (limit.getOffset() + limit.getLimit() < size()) {
                return new PlateBuffer(subList(limit.getOffset(), limit.getOffset() + limit.getLimit()));
            }else if (limit.getOffset()>0){
                return new PlateBuffer(subList(limit.getOffset(), size()));
            }
        }
        return this;
    }


    @Override
    public <M extends Entity, F> F readField(BufferQueryField<M, F> query) {
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        return readFieldFromBuffer(this, query);
    }

    @Override
    public Plates subList(int fromIndex, int toIndex) {
        return new PlateBuffer(plateList.subList(fromIndex, toIndex));
    }

    @Override
    public <O extends Entity, F> List<F> readFieldList(BufferQueryField<O, F> query) {
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        return readFieldList2(query);
    }

    public <F, E extends Entity> List<F> readFieldList2(BufferQueryField<E, F> query) {
        return resultFieldListFromBuffer(query);
    }

    @Override
    public PlateBuffer readPlateList(BufferQueryPlate query) {
        getAccessProtector().removeViolatedInfoColumns(query.getFields(), OperationType.READ);
        if (!query.isSingleEntity()) {
            throw new BufferedOperationAllowedOnlyForSingleEntityColumns();
        }
        return resultPlateList(query);
    }

    @Override
    public <E extends Entity> int delete(BufferQueryDelete<E> query, boolean transaction) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.DELETE);
        List<E> toRemove = query.toSelect().findMany();
        toRemove.forEach(this::remove);
        return toRemove.size();
    }

    @Override
    public <E extends Entity> int update(BufferQueryUpdate<E> query, boolean transaction) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.UPDATE);
        return 0;
    }

    @Override
    public <E extends Entity> E insert(BufferQueryInsert<E> query, boolean transaction) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.INSERT);
        return null;
    }

    @Override
    public <E extends Entity> int delete(BufferQueryDelete<E> query, Propagation propagation) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.DELETE);
        List<E> toRemove = query.toSelect().findMany();
        toRemove.forEach(this::remove);
        return toRemove.size();
    }


    @Override
    public <E extends Entity> int update(BufferQueryUpdate<E> query, Propagation propagation) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.UPDATE);
        return 0;
    }

    @Override
    public <E extends Entity> E insert(BufferQueryInsert<E> query, Propagation propagation) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.INSERT);
        return null;
    }


    @Override
    public OpenedTransactionBufferTarget beginTransaction() {
        //throw
        return null;
    }

    @Override
    public List<Entity> getTables() {
        return new LinkedList<>();
    }

    @Override
    public Plates findMany() {
        return this;
    }

    @Override
    public Optional<Plate> findFirst() {
        return isEmpty() ? Optional.empty() : Optional.of(get(0));
    }

    @Override
    public Plates calculateIndexes() {
        return null;
    }

    @Override
    public long countAny() {
        return size();
    }

    @Override
    public Plate readPlate(BufferQueryPlate query) {
        getAccessProtector().removeViolatedInfoColumns(query.getFields(), OperationType.READ);
        if (!query.isSingleEntity()) {
            throw new BufferedOperationAllowedOnlyForSingleEntityColumns();
        }
        return readPlateFromBuffer(query);
    }


    @Override
    public Logger getLog() {
        return log;
    }


    @Override
    public String toJSON() {
        StringBuilder rv = new StringBuilder();
        rv.append("[");

        int size = size();
        int cnt = 0;

        for (Plate val : this) {

            cnt++;
            boolean lastOne = cnt == size;

            rv.append(val.toJSON());
            if (!lastOne) rv.append(",");
        }

        rv.append("]");

        return rv.toString();

    }


    @Override
    public PlateBuffer copy() {
        return new PlateBuffer(this);
    }

    @Override
    public Plates clone() {
        if (!isEmpty()) {
            throw new DaobabException("Only " + EntityMap.class.getName() + " entities may be cloned");
        }
        return new PlateBuffer(EntityDuplicator.clonePlateList(this));
    }


    @Override
    public StatisticCollector getStatisticCollector() {
        if (statistic == null) {
            statistic = new StatisticCollectorImpl();
        }
        return statistic;
    }

    @Override
    public boolean isStatisticCollectingEnabled() {
        return statisticEnabled;
    }

    @Override
    public void enableStatisticCollecting(boolean statisticEnabled) {
        this.statisticEnabled = statisticEnabled;
    }


    @Override
    public int size() {
        return plateList.size();
    }

    @Override
    public boolean isEmpty() {
        return plateList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return plateList.contains(o);
    }

    @Override
    public Iterator<Plate> iterator() {
        return plateList.iterator();
    }

    @Override
    public Object[] toArray() {
        return plateList.toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return plateList.toArray(ts);
    }

    @Override
    public boolean add(Plate plate) {
        return plateList.add(plate);
    }

    @Override
    public boolean remove(Object o) {
        return plateList.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return plateList.containsAll(collection);
    }

    @Override
    public boolean addAll(Collection<? extends Plate> collection) {
        return plateList.addAll(collection);
    }

    @Override
    public boolean addAll(int i, Collection<? extends Plate> collection) {
        return plateList.addAll(i, collection);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return plateList.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return plateList.retainAll(collection);
    }

    @Override
    public void clear() {
        plateList.clear();
    }

    @Override
    public Plate get(int i) {
        return plateList.get(i);
    }

    @Override
    public Plate set(int i, Plate plate) {
        return plateList.set(i, plate);
    }

    @Override
    public void add(int i, Plate plate) {
        plateList.add(i, plate);
    }

    @Override
    public Plate remove(int i) {
        return plateList.remove(i);
    }

    @Override
    public int indexOf(Object o) {
        return plateList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return plateList.lastIndexOf(o);
    }

    @Override
    public ListIterator<Plate> listIterator() {
        return plateList.listIterator();
    }

    @Override
    public ListIterator<Plate> listIterator(int i) {
        return plateList.listIterator(i);
    }

    @Override
    public Entities<StatisticRecord> getStatistics() {
        return getStatisticCollector().getTarget();
    }


    @Override
    public AccessProtector getAccessProtector() {
        return accessProtector;
    }

    public void setAccessProtector(AccessProtector accessProtector) {
        this.accessProtector = accessProtector;
    }


}
