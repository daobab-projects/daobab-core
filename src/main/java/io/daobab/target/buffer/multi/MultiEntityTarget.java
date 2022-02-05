package io.daobab.target.buffer.multi;

import io.daobab.error.TargetNotSupports;
import io.daobab.model.*;
import io.daobab.query.base.Query;
import io.daobab.result.EntitiesJoined;
import io.daobab.target.BaseTarget;
import io.daobab.target.buffer.BufferQueryTarget;
import io.daobab.target.buffer.bytebyffer.PlateBufferIndexed;
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
        Entities<E> cached = getEntities(query.getEntityClass());

        if (query.getJoins().isEmpty()) {
            return cached.readEntityList(query);
        } else {
            Plates plates = makeJoinJob(query, cached);
            List<E> entityList = plates.stream().map(p -> p.getEntity(query.getEntityClass())).collect(Collectors.toList());
            return new EntityList<>(entityList, query.getEntityClass());
        }
    }

    public <E extends Entity> E readEntity(BufferQueryEntity<E> query) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.READ);
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        Entities<E> cached = getEntities(query.getEntityClass());

        if (query.getJoins().isEmpty()) {
            return cached.readEntity(query);
        } else {
            Plates plates = makeJoinJob(query, cached);
            if (plates.isEmpty()) {
                return null;
            }
            return plates.get(0).getEntity(query.getEntityClass());
        }
    }

    @SuppressWarnings("java:S1168")
    public Plate readPlate(BufferQueryPlate query) {
        getAccessProtector().removeViolatedInfoColumns(query.getFields(), OperationType.READ);
        Entities<?> cached = getEntities(query.getEntityClass());
        if (query.getJoins().isEmpty()) {
            return cached.readPlate(query);
        } else {
            Plates plates = makeJoinJob(query, cached);
            if (plates.isEmpty()) {
                return null;
            }
            return plates.readPlate(query);
        }
    }

    @SuppressWarnings("unchecked")
    public <E extends Entity, F> F readField(BufferQueryField<E, F> query) {
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        Entities<E> cached = getEntities(query.getEntityClass());
        if (query.getJoins().isEmpty()) {
            return cached.readField(query);
        } else {
            Plates plates = makeJoinJob(query, cached);
            if (plates.isEmpty()) {
                return null;
            }
            Column<?, ?, ?> col = query.getFields().get(0).getColumn();
            return (F) plates.get(0).getValue(col);
        }
    }

    public <E extends Entity> E insert(BufferQueryInsert<E> query, boolean transaction) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.INSERT);
        Entities<E> cached = getEntities(query.getEntityClass());
        return cached.insert(query, transaction);
    }

    public <E extends Entity> E insert(BufferQueryInsert<E> query, Propagation propagation) {
        throw new TargetNotSupports();
    }

    public <E extends Entity> int update(BufferQueryUpdate<E> query, boolean transaction) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.UPDATE);
        Entities<E> cached = getEntities(query.getEntityClass());
        return cached.update(query, transaction);
    }

    public <E extends Entity> int update(BufferQueryUpdate<E> query, Propagation propagation) {
        throw new TargetNotSupports();
    }

    @SuppressWarnings("unchecked")
    public <E extends Entity, F> List<F> readFieldList(BufferQueryField<E, F> query) {
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        Entities<E> cached = getEntities(query.getEntityClass());
        if (query.getJoins().isEmpty()) {
            return cached.readFieldList(query);
        } else {
            Column<?, ?, ?> col = query.getFields().get(0).getColumn();
            return (List<F>) makeJoinJob(query, cached).stream().map(p -> p.getValue(col)).collect(Collectors.toList());
        }
    }

    public Plates readPlateList(BufferQueryPlate query) {
        getAccessProtector().removeViolatedInfoColumns(query.getFields(), OperationType.READ);
        Entities<?> cached = getEntities(query.getEntityClass());

        if (query.getJoins().isEmpty()) {
            return cached.readPlateList(query);
        } else {
            return makeJoinJob(query, cached);
        }
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
        Entities<E> cached = getEntities(query.getEntityClass());
        return cached.delete(query, transaction);
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
