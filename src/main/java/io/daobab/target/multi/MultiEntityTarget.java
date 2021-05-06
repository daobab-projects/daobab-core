package io.daobab.target.multi;

import io.daobab.error.*;
import io.daobab.model.Entity;
import io.daobab.model.Plate;
import io.daobab.query.*;
import io.daobab.query.base.Query;
import io.daobab.result.Entities;
import io.daobab.result.Plates;
import io.daobab.target.BaseTarget;
import io.daobab.target.OpenedTransactionTarget;
import io.daobab.target.QueryTarget;
import io.daobab.target.database.TransactionalTarget;
import io.daobab.target.protection.AccessProtector;
import io.daobab.target.protection.OperationType;
import io.daobab.transaction.Propagation;

import java.util.*;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class MultiEntityTarget extends BaseTarget implements MultiEntity, QueryTarget {

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
    public boolean isBuffer() {
        return true;
    }

    @Override
    public boolean isConnectedToDatabase() {
        return false;
    }

    @Override
    public boolean isLogQueriesEnabled() {
        return false;
    }

    @Override
    public OpenedTransactionTarget beginTransaction() {
        return null;
    }

    protected void register(Class<? extends Entity>... entityClazz) {
        if (entityClazz == null) return;
        for (Class<? extends Entity> entity : entityClazz) {
            getStorage().put(entity, null);
        }
    }

    protected void register(Entities<? extends Entity>... entities) {
        if (entities == null) return;
        for (Entities<? extends Entity> entity : entities) {
            getStorage().put(entity.getEntityClazz(), entity);
        }
    }


    @Override
    public <E extends Entity> boolean isRegistered(Class<E> entityClass) {
        if (!isRegistered(entityClass)) {
            throw new DaobabException(String.format("Entity %s is not registered into %s. Register entity or put related collection into.", entityClass.getName(), this.getClass().getName()));
        }
        return storage.keySet().contains(entityClass);
    }


    @Override
    public <E extends Entity> Entities<E> getEntities(E entity) {
        Entities<E> cached = (Entities<E>) getStorage().get(entity.getClass());

        return cached;
    }


    @Override
    public <E extends Entity> Entities<E> getEntities(Class<E> entityClazz) {
        Entities<E> cached = (Entities<E>) getStorage().get(entityClazz);

        return cached;
    }


    public <E extends Entity> Entities<E> readEntityList(QueryEntity<E> query) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.READ);
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        Entities<E> cached = getEntities(query.getEntityClass());
        return cached.readEntityList(query);
    }


    public <E extends Entity> E readEntity(QueryEntity<E> query) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.READ);
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        Entities<E> cached = getEntities(query.getEntityClass());
        return cached.readEntity(query);
    }

    public Plate readPlate(QueryPlate query) {
        getAccessProtector().removeViolatedInfoColumns(query.getFields(), OperationType.READ);
        Entities<?> cached = getEntities(query.getEntityClass());
        return cached.readPlate(query);
    }


    public <E extends Entity, F> F readField(QueryField<E, F> query) {
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        Entities<E> cached = getEntities(query.getEntityClass());
        return cached.readField(query);
    }

    public <E extends Entity> E insert(QueryInsert<E> query, boolean transaction) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.INSERT);
        Entities<E> cached = getEntities(query.getEntityClass());
        return cached.insert(query, transaction);
    }


    public <E extends Entity> E insert(QueryInsert<E> query, Propagation propagation) {
        if (!(this instanceof TransactionalTarget)) throw new TargetUntransactional(this);
        Entities<E> cached = getEntities(query.getEntityClass());
        return handleTransactionalTarget((TransactionalTarget) cached, propagation, (target, transaction) -> target.insert(query, transaction));
    }


    public <E extends Entity> int update(QueryUpdate<E> query, boolean transaction) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.UPDATE);
        Entities<E> cached = getEntities(query.getEntityClass());
        return cached.update(query, transaction);
    }

    public <E extends Entity> int update(QueryUpdate<E> query, Propagation propagation) {
        Entities<E> cached = getEntities(query.getEntityClass());
        return handleTransactionalTarget((TransactionalTarget) cached, propagation, (target, transaction) -> target.update(query, transaction));
    }

    public <E extends Entity, F> List<F> readFieldList(QueryField<E, F> query) {
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        Entities<E> cached = getEntities(query.getEntityClass());
        return cached.readFieldList(query);
    }

    public Plates readPlateList(QueryPlate query) {
        getAccessProtector().removeViolatedInfoColumns(query.getFields(), OperationType.READ);
        Entities<?> cached = getEntities(query.getEntityClass());
        return cached.readPlateList(query);
    }

    public <E extends Entity> int delete(QueryDelete<E> query, boolean transaction) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.DELETE);
        Entities<E> cached = getEntities(query.getEntityClass());
        return cached.delete(query, transaction);
    }

    public <E extends Entity> int delete(QueryDelete<E> query, Propagation propagation) {
        Entities<E> cached = getEntities(query.getEntityClass());
        return handleTransactionalTarget((TransactionalTarget) cached, propagation, (target, transaction) -> target.delete(query, transaction));
    }


    @Override
    public List<Entity> getTables() {
        List<Entity> tables = new ArrayList<>();
        return tables;
    }


    public Map<Class<? extends Entity>, Entities<? extends Entity>> getStorage() {
        return storage;
    }

    @Override
    public <E extends Entity> String toSqlQuery(Query<E, ?> query) {
        throw new DaobabException("This target does not produce sql query");
    }

    @Override
    @SafeVarargs
    public final void put(Entities<? extends Entity>... entities) {
        if (entities == null) return;
        for (Entities<? extends Entity> e : entities) {
            getStorage().put(e.getEntityClazz(), e);
        }
    }


}
