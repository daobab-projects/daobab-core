package io.daobab.target.buffer.multi;

import io.daobab.error.TargetNotSupports;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.Plate;
import io.daobab.model.TableColumn;
import io.daobab.query.base.Query;
import io.daobab.result.EntitiesJoined;
import io.daobab.target.BaseTarget;
import io.daobab.target.buffer.BufferQueryTarget;
import io.daobab.target.buffer.noheap.PlateBufferIndexed;
import io.daobab.target.buffer.query.*;
import io.daobab.target.buffer.single.Entities;
import io.daobab.target.buffer.single.EntityList;
import io.daobab.target.buffer.single.PlateBuffer;
import io.daobab.target.buffer.single.Plates;
import io.daobab.target.buffer.transaction.OpenedTransactionBufferTarget;
import io.daobab.target.protection.OperationType;
import io.daobab.transaction.Propagation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class MultiEntityTarget extends BaseTarget implements MultiEntity, BufferQueryTarget {

    private final Map<Class<? extends Entity>, Entities<? extends Entity>> storage = new HashMap<>();

    public MultiEntityTarget() {
    }

    @SafeVarargs
    public MultiEntityTarget(Entities<? extends Entity>... entities) {
        if (entities == null) return;
        for (Entities<? extends Entity> entity : entities) {
            register(entity);
        }
    }

    @Override
    public boolean isLogQueriesEnabled() {
        return false;
    }

    @Override
    public OpenedTransactionBufferTarget beginTransaction() {
        return null;
    }

    @SafeVarargs
    protected final void register(boolean lazy, Class<? extends Entity>... entityClazz) {
        register(entityClazz);
        if (!lazy) {
            for (Class<? extends Entity> clazz : entityClazz) {
                getEntities(clazz);
            }
        }

    }

    @SafeVarargs
    protected final void register(Class<? extends Entity>... entityClazz) {
        if (entityClazz == null) return;
        for (Class<? extends Entity> entity : entityClazz) {
            getStorage().put(entity, null);
        }
    }

    @SafeVarargs
    protected final void register(Entities<? extends Entity>... entities) {
        if (entities == null) return;
        for (Entities<? extends Entity> entity : entities) {
            getStorage().put(entity.getEntityClass(), entity);
        }
    }

    @Override
    public <E extends Entity> boolean isRegistered(Class<E> entityClass) {
        return storage.containsKey(entityClass);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Entity> Entities<E> getEntities(E entity) {
        return (Entities<E>) getStorage().get(entity.getClass());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Entity> Entities<E> getEntities(Class<E> entityClazz) {
        return (Entities<E>) getStorage().get(entityClazz);
    }

    public <E extends Entity> Entities<E> readEntityList(BufferQueryEntity<E> query) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.READ);
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        Entities<E> entities = getEntities(query.getEntityClass());

        Entities<E> results;
        if (query.getJoins().isEmpty()) {
            results = entities.readEntityList(query);
        } else {
            Plates plates = makeJoinJob(query, entities);
            List<E> entityList = plates.stream().map(p -> p.getEntity(query.getEntityClass())).collect(Collectors.toList());
            results = new EntityList<>(entityList, query.getEntityClass());
        }
        if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, results.size());
        return results;
    }

    public <E extends Entity> E readEntity(BufferQueryEntity<E> query) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.READ);
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        Entities<E> entities = getEntities(query.getEntityClass());

        E result;
        if (query.getJoins().isEmpty()) {
            result = entities.readEntity(query);
        } else {
            Plates plates = makeJoinJob(query, entities);
            if (plates.isEmpty()) {
                if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, 0);
                return null;
            }
            result = plates.get(0).getEntity(query.getEntityClass());
        }
        if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, result == null ? 0 : 1);
        return result;
    }

    @SuppressWarnings("java:S1168")
    public Plate readPlate(BufferQueryPlate query) {
        getAccessProtector().removeViolatedInfoColumns(query.getFields(), OperationType.READ);
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        Entities<?> entities = getEntities(query.getEntityClass());

        Plate results;
        if (query.getJoins().isEmpty()) {
            results = entities.readPlate(query);
        } else {
            Plates plates = makeJoinJob(query, entities);
            if (plates.isEmpty()) {
                if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, 0);
                return null;
            }
            results = plates.readPlate(query);
        }
        if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, results == null ? 0 : 1);
        return results;
    }

    @SuppressWarnings("unchecked")
    public <E extends Entity, F> F readField(BufferQueryField<E, F> query) {
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        Entities<E> entities = getEntities(query.getEntityClass());

        F result;
        if (query.getJoins().isEmpty()) {
            result = entities.readField(query);
        } else {
            Plates plates = makeJoinJob(query, entities);
            if (plates.isEmpty()) {
                if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, 0);
                return null;
            }
            Column<?, ?, ?> col = query.getFields().get(0).getColumn();
            result = (F) plates.get(0).getValue(col);
        }
        if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, result == null ? 0 : 1);
        return result;
    }

    public <E extends Entity> E insert(BufferQueryInsert<E> query, boolean transaction) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.INSERT);
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        Entities<E> entities = getEntities(query.getEntityClass());
        E result = entities.insert(query, transaction);
        if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, result == null ? 0 : 1);
        return result;
    }

    public <E extends Entity> E insert(BufferQueryInsert<E> query, Propagation propagation) {
        throw new TargetNotSupports();
    }

    public <E extends Entity> int update(BufferQueryUpdate<E> query, boolean transaction) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.UPDATE);
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        Entities<E> entities = getEntities(query.getEntityClass());
        int result = entities.update(query, transaction);
        if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, result);
        return result;
    }

    public <E extends Entity> int update(BufferQueryUpdate<E> query, Propagation propagation) {
        throw new TargetNotSupports();
    }

    @SuppressWarnings("unchecked")
    public <E extends Entity, F> List<F> readFieldList(BufferQueryField<E, F> query) {
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        Entities<E> entities = getEntities(query.getEntityClass());

        List<F> results;
        if (query.getJoins().isEmpty()) {
            results = entities.readFieldList(query);
        } else {
            Column<?, ?, ?> col = query.getFields().get(0).getColumn();
            results = (List<F>) makeJoinJob(query, entities).stream().map(p -> p.getValue(col)).collect(Collectors.toList());
        }
        if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, results.size());
        return results;
    }

    public Plates readPlateList(BufferQueryPlate query) {
        getAccessProtector().removeViolatedInfoColumns(query.getFields(), OperationType.READ);
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        Entities<?> entities = getEntities(query.getEntityClass());

        Plates results;
        if (query.getJoins().isEmpty()) {
            results = entities.readPlateList(query);
        } else {
            results = makeJoinJob(query, entities);
        }
        if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, results.size());
        return results;
    }

    @SuppressWarnings("unchecked")
    private PlateBuffer makeJoinJob(Query<?, ?, ?> query, List<? extends Entity> entities) {
        EntitiesJoined entitiesJoined = new EntitiesJoined(this, entities, query);

        PlateBuffer matched = new PlateBuffer(PlateBufferIndexed.finalFilter(entitiesJoined.results(), query));
        if (matched.isEmpty()) {
            if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, 0);
            return new PlateBuffer();
        }

        Plates elements = matched.orderAndLimit(query);
        List<Plate> results = new ArrayList<>(elements.size());

        for (Plate element : elements) {
            Plate plate = new Plate(query.getFields());
            for (TableColumn tableColumn : query.getFields()) {
                plate.setValue(tableColumn, element.getValue(tableColumn.getColumn()));
            }
            results.add(plate);
        }
        return new PlateBuffer(results);
    }

    public <E extends Entity> int delete(BufferQueryDelete<E> query, boolean transaction) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.DELETE);
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        Entities<E> entities = getEntities(query.getEntityClass());
        int result = entities.delete(query, transaction);
        if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, result);
        return result;
    }

    public <E extends Entity> int delete(BufferQueryDelete<E> query, Propagation propagation) {
        throw new TargetNotSupports();
    }

    @Override
    public List<Entity> getTables() {
        return new ArrayList<>();
    }

    public Map<Class<? extends Entity>, Entities<? extends Entity>> getStorage() {
        return storage;
    }

    @Override
    @SafeVarargs
    public final void put(Entities<? extends Entity>... entities) {
        if (entities == null) return;
        for (Entities<? extends Entity> e : entities) {
            getStorage().put(e.getEntityClass(), e);
        }
    }

}
