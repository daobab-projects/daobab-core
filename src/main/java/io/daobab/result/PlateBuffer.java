package io.daobab.result;

import io.daobab.clone.EntityDuplicator;
import io.daobab.error.BufferedOperationAllowedOnlyForSingleEntityColumns;
import io.daobab.error.DaobabException;
import io.daobab.model.*;
import io.daobab.query.*;
import io.daobab.query.base.Query;
import io.daobab.statement.condition.Limit;
import io.daobab.target.OpenedTransactionTarget;
import io.daobab.target.protection.AccessProtector;
import io.daobab.target.protection.BasicAccessProtector;
import io.daobab.target.protection.OperationType;
import io.daobab.target.statistic.StatisticProvider;
import io.daobab.target.statistic.StatisticCollector;
import io.daobab.target.statistic.StatisticCollectorImpl;
import io.daobab.target.statistic.StatisticCollectorProvider;
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
    protected transient boolean needRefresh = false;
    private boolean transactionActive = false;
    private transient Logger log;

    private StatisticCollector statistic;
    private boolean statisticEnabled = false;

    private AccessProtector accessProtector = new BasicAccessProtector();


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

    @Override
    public <E1 extends Entity> E1 readEntity(QueryEntity<E1> query) {
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
    public <E extends Entity> Entities<E> readEntityList(QueryEntity<E> query) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.READ);
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        List<Plate> plateList = filter(query);
        List<E> rrv = new ArrayList<>(plateList.size());
        for (int i = 0; i < plateList.size(); i++) {
            rrv.add(i, (E) plateList.get(i).getEntity(query.getEntityClass()));
        }

        EntityList<E> rv = new EntityList<>(rrv, query.getEntityClass());

        rv.orderAndLimit(query);
        if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, rv.size());
        return rv;
    }


    private <E extends Entity, R extends EntityRelation> PlateBuffer resultPlateList(QueryPlate query) {
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        PlateBuffer matched = new PlateBuffer(filter((Query<E, ?>) query));
        List<Plate> frl = new LinkedList<>();

        if (matched.isEmpty()) return new PlateBuffer(frl);
        Plates el = matched.orderAndLimit((Query<E, ?>) query);


        for (Plate e : el) {
            Plate fr = new Plate(query.getFields());

            for (TableColumn c : query.getFields()) {
                Column<E, Object, R> col = (Column<E, Object, R>) c.getColumn();
                fr.setValue(c, col.getValueOf((R) e));
            }
            frl.add(fr);
        }
        if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, frl.size());
        return new PlateBuffer(frl);
    }

    private Plate readPlateFromBuffer(QueryPlate query) {
        PlateBuffer proj = resultPlateList(query);
        return proj.isEmpty() ? null : proj.get(0);
    }

    private <E extends Entity, R extends EntityRelation, F> List<F> resultFieldListFromBuffer(QueryField<E, F> query) {
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        PlateBuffer matched = new PlateBuffer(filter(query));

        if (matched.isEmpty()) return new LinkedList<>();
        Plates el = matched.orderAndLimit(query);
        Column<E, F, R> col = (Column<E, F, R>) query.getFields().get(0);
        List<F> rv = el.stream().map(e -> col.getValueOf((R) e)).collect(Collectors.toList());
        if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, rv.size());
        return rv;

    }

    private <E extends Entity, R extends EntityRelation, F> F readFieldFromBuffer(Plates entities, QueryField<E, F> query) {
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

        Column<E, F, R> col = (Column<E, F, R>) query.getFields().get(0);
        F rv = col.getValueOf((R) el);
        if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, 1);
        return rv;
    }

    @SuppressWarnings("unchecked")
    public <E extends Entity> Plates orderAndLimit(Query<E, ?> query) {
        if (query == null) return this;
        PlateBuffer copy = new PlateBuffer(this);
        if (query.getOrderBy() != null) {
//            @SuppressWarnings("rawtypes")
//            OrderComparator<Projection> comparator = new OrderComparator(query.getOrderBy().toOrderFieldList());
//            copy.sort(comparator);

            return copy.limit(query);
        }
        return copy;
    }

    @Override
    public Plates limit(QueryPlate query) {
        return null;
    }

    public <E extends Entity> Plates limit(Query<E, ?> query) {
        if (query == null) return this;

        if (query.getLimit() != null) {
            Limit limit = query.getLimit();
            if (limit.getLimit() < size()) {
                return new PlateBuffer(subList(limit.getOffset(), limit.getOffset() + limit.getLimit()));
            }
        }
        return this;
    }

    @Override
    public <M extends Entity, F> F readField(QueryField<M, F> query) {
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        return readFieldFromBuffer(this, query);
    }

    @Override
    public Plates subList(int fromIndex, int toIndex) {
        return new PlateBuffer(plateList.subList(fromIndex, toIndex));
    }

    @Override
    public <O extends Entity, F> List<F> readFieldList(QueryField<O, F> query) {
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        return readFieldList2(query);
    }

    public <F, E extends Entity> List<F> readFieldList2(QueryField<E, F> query) {
        return resultFieldListFromBuffer(query);
    }

    @Override
    public PlateBuffer readPlateList(QueryPlate query) {
        getAccessProtector().removeViolatedInfoColumns(query.getFields(), OperationType.READ);
        if (!query.isSingleEntity()) {
            throw new BufferedOperationAllowedOnlyForSingleEntityColumns();
        }
        return resultPlateList(query);
    }

    @Override
    public <E extends Entity> int delete(QueryDelete<E> query, boolean transaction) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.DELETE);
        List<E> toRemove = query.toSelect().findMany();
        toRemove.forEach(this::remove);
        return toRemove.size();
    }

    @Override
    public <E extends Entity> int update(QueryUpdate<E> query, boolean transaction) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.UPDATE);
        return 0;
    }

    @Override
    public <E extends Entity> E insert(QueryInsert<E> query, boolean transaction) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.INSERT);
        return null;
    }

    @Override
    public <E extends Entity> int delete(QueryDelete<E> query, Propagation propagation) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.DELETE);
        List<E> toRemove = query.toSelect().findMany();
        toRemove.forEach(this::remove);
        return toRemove.size();
    }


    @Override
    public <E extends Entity> int update(QueryUpdate<E> query, Propagation propagation) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.UPDATE);
        return 0;
    }

    @Override
    public <E extends Entity> E insert(QueryInsert<E> query, Propagation propagation) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.INSERT);
        return null;
    }

    @Override
    public boolean isConnectedToDatabase() {
        return false;
    }

    @Override
    public OpenedTransactionTarget beginTransaction() {
        //throw
        return null;
    }

    @Override
    public List<Entity> getTables() {
        return new LinkedList<>();
    }


    @Override
    public boolean isBuffer() {
        return true;
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
    public void refreshImmediatelly() {
        // no action here

    }


    @Override
    public void markRefreshCache() {
        //if (isCached()==false) throw new DeveloperException("You can callDaobabServer this method for cached DAO's only. DAO '"+getEntityClassSimpleName()+"' is not cached.");
//		getLog().info("DAO "+getEntityClassSimpleName()+" marked as refresh needed.");
        needRefresh = true;
    }

    @Override
    public Plate readPlate(QueryPlate query) {
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
    public <E extends Entity> String toSqlQuery(Query<E, ?> query) {
        throw new DaobabException("This target does not produce sql query");
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


    @Override
    public <Out extends ProcedureParameters, In extends ProcedureParameters> Out callProcedure(String name, In in, Out out) {
        throw new DaobabException("This target does not supports procedures.");
    }
}
