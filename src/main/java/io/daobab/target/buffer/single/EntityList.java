package io.daobab.target.buffer.single;

import io.daobab.clone.EntityDuplicator;
import io.daobab.error.BufferedOperationAllowedOnlyForSingleEntityColumns;
import io.daobab.error.DaobabException;
import io.daobab.error.TargetNotSupports;
import io.daobab.model.*;
import io.daobab.query.base.Query;
import io.daobab.result.EntitiesBufferIndexed;
import io.daobab.statement.condition.Limit;
import io.daobab.statement.condition.base.OrderComparator;
import io.daobab.target.buffer.transaction.OpenedTransactionBufferTarget;
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Buffered Entity collection.
 *
 * @param <E> - Entity class
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class EntityList<E extends Entity> extends EntitiesBufferIndexed<E> implements Entities<E>, StatisticCollectorProvider, StatisticProvider {

    private static final long serialVersionUID = 2291798166104201910L;
    protected transient boolean needRefresh = false;
    private final transient Class<E> entityClazz;
    private final boolean transactionActive = false;
    private transient StatisticCollector statistic;
    private boolean statisticEnabled = false;
    private transient AccessProtector accessProtector = new BasicAccessProtector();

    public EntityList(List<E> entities, E entityinstance) {
        this(entities, (Class<E>) entityinstance.getClass());
    }

    public EntityList(List<E> entities, Class<E> entityClass) { //clear();
        super(entities);
        this.entityClazz = entityClass;
    }

    public EntityList(Class<E> entityClass) {
        this(new ArrayList<>(),entityClass);
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
        EntityList<E> results= new EntityList<>(filter((Query<E,?,?>) query), (Class<E>)query.getEntityClass());
        results.orderAndLimit((Query<E,?,?>) query);
        if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, 1);
        return (E1) results.get(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E1 extends Entity> Entities<E1> readEntityList(BufferQueryEntity<E1> query) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.READ);
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        EntityList<E> rv = new EntityList<>(filter((Query<E,?,?>) query), (Class<E>)query.getEntityClass());
        rv.orderAndLimit((Query<E,?,?>) query);
        Entities<E1> rt = (Entities<E1>) rv;
        if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, rv.size());
        return rt;
    }

    @SuppressWarnings({"unchecked","rawtypes"})
    private <R extends EntityRelation> PlateBuffer resultPlates(BufferQueryPlate query) {
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        EntityList<E> matched = new EntityList<>(filter((Query<E,?,?>) query), (Class<E>)query.getEntityClass());

        if (matched.isEmpty()){
            if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, 0);
            return new PlateBuffer();
        }

        List<Plate> results = new ArrayList<>();

        for (E entity : matched.orderAndLimit((Query<E,?,?>) query)) {
            Plate plate = new Plate(query.getFields());
            for (TableColumn tableColumn : query.getFields()) {
                plate.setValue(tableColumn, tableColumn.getColumn().getValueOf((R) entity));
            }
            results.add(plate);
        }

        if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, results.size());
        return new PlateBuffer(results);
    }

    private Plate readPlateFromBuffer(BufferQueryPlate query) {
        PlateBuffer proj = resultPlates(query);
        return proj.isEmpty() ? null : proj.get(0);
    }

    @SuppressWarnings({"unchecked","rawtypes"})
    private <R extends EntityRelation, F> List<F> resultFieldListFromBuffer(BufferQueryField<E, F> query) {
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        EntityList<E> matched = new EntityList<>(filter(query), query.getEntityClass());
        if (matched.isEmpty()){
            if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, 0);
            return new ArrayList<>();
        }
        Entities<E> entities = matched.orderAndLimit(query);
        Column<E, F, R> firstColumn = query.getFields().get(0).getColumn();
        List<F> results = entities.stream().map(e -> firstColumn.getValueOf((R) e)).collect(Collectors.toList());
        if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, results.size());
        return results;
    }

    @SuppressWarnings({"unchecked","rawtypes"})
    private <R extends EntityRelation, F> F readFieldFromBuffer(Entities<E> entities, BufferQueryField<E, F> query) {
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        EntityList<E> matched = new EntityList<>(entities.filter(query), query.getEntityClass());

        if (matched.isEmpty()) {
            if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, 0);
            return null;
        }
        E el = matched.orderAndLimit(query).get(0);
        if (el == null) {
            if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, 0);
            return null;
        }

        Column<E, F, R> firstColumn = query.getFields().get(0).getColumn();
        F rv = firstColumn.getValueOf((R) el);
        if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, 1);
        return rv;
    }

    @SuppressWarnings("unchecked")
    public Entities<E> orderAndLimit(Query<E,?,?> query) {
        if (query == null) return this;
        if (query.getOrderBy() != null) {
            EntityList<E> copy = new EntityList<>(this, this.entityClazz);
            @SuppressWarnings("rawtypes")
            OrderComparator<E> comparator = new OrderComparator(query.getOrderBy().toOrderFieldList());
            copy.sort(comparator);
            return copy.limit(query);
        }else{
            return limit(query);
        }
    }

    public Entities<E> limit(Query<E,?,?> query) {
        if (query != null && query.getLimit() != null) {
            Limit limit = query.getLimit();
            if (limit.getOffset() + limit.getLimit() < size()) {
                return new EntityList<>(subList(limit.getOffset(), limit.getOffset() + limit.getLimit()), this.entityClazz);
            }else if (limit.getOffset()>0){
                return new EntityList<>(subList(limit.getOffset(), size()), this.entityClazz);
            }
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <M extends Entity, F> F readField(BufferQueryField<M, F> query) {
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        F result = readFieldFromBuffer(this, (BufferQueryField<E, F>) query);
        if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, result==null?0:1);
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <O extends Entity, F> List<F> readFieldList(BufferQueryField<O, F> query) {
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        List<F> results = readFieldList2((BufferQueryField<E, F>) query);
        if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, results.size());
        return results;
    }

    public <F> List<F> readFieldList2(BufferQueryField<E, F> query) {
        return resultFieldListFromBuffer(query);
    }

    @Override
    public PlateBuffer readPlateList(BufferQueryPlate query) {
        getAccessProtector().removeViolatedInfoColumns(query.getFields(), OperationType.READ);
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        if (!query.isSingleEntity()) {
            throw new BufferedOperationAllowedOnlyForSingleEntityColumns();
        }
        PlateBuffer rv = resultPlates(query);
        if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, rv.size());
        return rv;
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
        throw new TargetNotSupports();
//        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.UPDATE);
//        return 0;
    }

    @Override
    public <E extends Entity> E insert(BufferQueryInsert<E> query, boolean transaction) {
        throw new TargetNotSupports();
//        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.INSERT);
//        return null;
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
        throw new TargetNotSupports();
//        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.UPDATE);
//        return 0;
    }

    @Override
    public <E extends Entity> E insert(BufferQueryInsert<E> query, Propagation propagation) {
        throw new TargetNotSupports();
//        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.INSERT);
//        return null;
    }

    @Override
    public OpenedTransactionBufferTarget beginTransaction() {
        throw new TargetNotSupports();
    }

    @Override
    public List<Entity> getTables() {
        return new LinkedList<>();
    }

    @Override
    public Class<E> getEntityClass() {
        return entityClazz;
    }

    @Override
    public Entities<E> findMany() {
        return this;
    }

    @Override
    public Optional<E> findFirst() {
        return isEmpty() ? Optional.empty() : Optional.of(get(0));
    }

    @Override
    public long countAny() {
        return size();
    }

    @Override
    public void refreshImmediately() {
        throw new TargetNotSupports();
    }

    @Override
    public void markRefreshCache() {
        needRefresh = true;
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

        if (!isEmpty() && !(get(0) instanceof EntityMap)) {
            throw new DaobabException("this method can be used for EntityMap implementations only");
        }

        for (E val : this) {
            cnt++;
            rv.append(((EntityMap) val).toJSON());
            if (cnt != size) rv.append(",");
        }

        rv.append("]");

        return rv.toString();
    }

    @Override
    public EntityList<E> copy() {
        return new EntityList<>(this, this.entityClazz);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Entities<E> clone() {
        if (!isEmpty() && !(get(0) instanceof EntityMap))
            throw new DaobabException("Only " + EntityMap.class.getName() + " entities may be cloned");
        List<EntityMap> srcList = (List<EntityMap>) this;
        return new EntityList<>((List<E>) EntityDuplicator.cloneEntityList(srcList), this.entityClazz);
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
