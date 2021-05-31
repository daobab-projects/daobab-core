package io.daobab.result;

import io.daobab.clone.EntityDuplicator;
import io.daobab.error.BufferedOperationAllowedOnlyForSingleEntityColumns;
import io.daobab.error.DaobabException;
import io.daobab.model.*;
import io.daobab.query.*;
import io.daobab.query.base.Query;
import io.daobab.statement.condition.Limit;
import io.daobab.statement.condition.base.OrderComparator;
import io.daobab.target.OpenedTransactionTarget;
import io.daobab.target.database.DataBaseTarget;
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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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


    private transient final Class<E> entityClazz;
    private boolean transactionActive = false;

    private transient Logger log;

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

    @Override
    public <E1 extends Entity> E1 readEntity(QueryEntity<E1> query) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.READ);
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        EntityList<E> rv = new EntityList<>(filter((Query<E, ?>) query), (Class<E>)query.getEntityClass());

        rv.orderAndLimit((Query<E, ?>) query);

        if (rv.isEmpty()) return null;
        if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, 1);
        return (E1) rv.get(0);
    }

    @Override
    public <E1 extends Entity> Entities<E1> readEntityList(QueryEntity<E1> query) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.READ);
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        EntityList<E> rv = new EntityList<>(filter((Query<E, ?>) query), (Class<E>)query.getEntityClass());
        rv.orderAndLimit((Query<E, ?>) query);
        Entities<E1> rt = (Entities<E1>) rv;
        if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, rv.size());
        return rt;
    }


    private <R extends EntityRelation> PlateBuffer resultPlates(QueryPlate query) {
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        EntityList<E> matched = new EntityList<>(filter((Query<E, ?>) query), (Class<E>)query.getEntityClass());
        List<Plate> frl = new LinkedList<>();

        if (matched == null || matched.size() == 0) return new PlateBuffer(frl);
        Entities<E> el = matched.orderAndLimit((Query<E, ?>) query);


        for (E e : el) {
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

    private Plate readPlateFromBuffer(Entities<E> daocached, QueryPlate query) {
        PlateBuffer proj = resultPlates(query);
        return proj.isEmpty() ? null : proj.get(0);
    }

    private <R extends EntityRelation, F> List<F> resultFieldListFromBuffer(QueryField<E, F> query) {
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        EntityList<E> matched = new EntityList<>(filter(query), query.getEntityClass());

        if (matched == null || matched.isEmpty()) return new LinkedList<>();
        Entities<E> el = matched.orderAndLimit(query);
        Column<E, F, R> col = (Column<E, F, R>) query.getFields().get(0).getColumn();
        List<F> rv = el.stream().map(e -> col.getValueOf((R) e)).collect(Collectors.toList());
        if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, rv.size());
        return rv;
    }

    private <R extends EntityRelation, F> F readFieldFromBuffer(Entities<E> entities, QueryField<E, F> query) {
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

        Column<E, F, R> col = (Column<E, F, R>) query.getFields().get(0).getColumn();
        F rv = col.getValueOf((R) el);
        if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, 1);
        return rv;
    }

    @SuppressWarnings("unchecked")
    public Entities<E> orderAndLimit(Query<E, ?> query) {
        if (query == null) return this;
        EntityList<E> copy = new EntityList<>(this, this.entityClazz);
        if (query.getOrderBy() != null) {
            @SuppressWarnings("rawtypes")
            OrderComparator<E> comparator = new OrderComparator(query.getOrderBy().toOrderFieldList());
            copy.sort(comparator);

            return copy.limit(query);
        }
        return copy;
    }

    public Entities<E> limit(Query<E, ?> query) {
        if (query == null) return this;

        if (query.getLimit() != null) {
            Limit limit = query.getLimit();
            if (limit.getLimit() < size()) {
                return new EntityList<>(subList(limit.getOffset(), limit.getOffset() + limit.getLimit()), this.entityClazz);
            }
        }
        return this;
    }

    @Override
    public <M extends Entity, F> F readField(QueryField<M, F> query) {
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        F rv = readFieldFromBuffer(this, (QueryField<E, F>) query);
        if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, 1);
        return rv;
    }


    @Override
    public <O extends Entity, F> List<F> readFieldList(QueryField<O, F> query) {
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        List<F> rv = readFieldList2((QueryField<E, F>) query);
        if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, rv.size());
        return rv;
    }

    public <F> List<F> readFieldList2(QueryField<E, F> query) {
        return resultFieldListFromBuffer(query);
    }

    @Override
    public PlateBuffer readPlateList(QueryPlate query) {
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
        // no action here
    }

    @Override
    public void markRefreshCache() {
        needRefresh = true;
    }

    @Override
    public Plate readPlate(QueryPlate query) {
        getAccessProtector().removeViolatedInfoColumns(query.getFields(), OperationType.READ);
        if (!query.isSingleEntity()) {
            throw new BufferedOperationAllowedOnlyForSingleEntityColumns();
        }
        return readPlateFromBuffer(this, query);
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
            boolean lastOne = cnt == size;

            rv.append(((EntityMap) val).toJSON());
            if (!lastOne) rv.append(",");
        }

        rv.append("]");

        return rv.toString();

    }


    @Override
    public EntityList<E> copy() {
        return new EntityList<>(this, this.entityClazz);
    }

    @Override
    public Entities<E> clone() {
        if (!isEmpty() && !(get(0) instanceof EntityMap))
            throw new DaobabException("Only " + EntityMap.class.getName() + " entities may be cloned");
        List<EntityMap> srcList = (List<EntityMap>) this;
        return new EntityList<>((List<E>) EntityDuplicator.cloneEntityList(srcList), this.entityClazz);
    }


    public String toUpdateSQL(DataBaseTarget target) {
        StringBuilder sb = new StringBuilder();
        if (isEmpty() || !(this.get(0) instanceof PrimaryKey)) {
            return sb.toString();
        }
        for (E entity : this) {
            PrimaryKey<E, ?, ?> pkentity = (PrimaryKey<E, ?, ?>) entity;
            if (pkentity.getId() == null) continue;
            sb.append(((PrimaryKey<E, ?, ?>) entity).getSqlUpdate(target));
        }

        return sb.toString();
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
    public <E extends Entity> String toSqlQuery(Query<E, ?> query) {
        throw new DaobabException("This target does not produce sql query");
    }

    @Override
    public Entities<StatisticRecord> getStatistics() {
        return getStatisticCollector().getTarget();
    }

    @Override
    public Class<E> getEntityClazz() {
        return entityClazz;
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
