package io.daobab.target.multi;

import io.daobab.error.DaobabEntityCreationException;
import io.daobab.error.EntityNotRegisteredIntoBuffer;
import io.daobab.error.SourceTargetUnderBufferedTargetMissed;
import io.daobab.error.TargetUntransactional;
import io.daobab.model.Entity;
import io.daobab.query.QueryDelete;
import io.daobab.query.QueryInsert;
import io.daobab.query.QueryUpdate;
import io.daobab.result.Entities;
import io.daobab.target.QueryTarget;
import io.daobab.target.database.TransactionalTarget;
import io.daobab.transaction.Propagation;

import java.util.List;

public abstract class AboveMultiEntityTarget extends QueryMultiEntityTarget implements AboveMultiEntity {

    private boolean propagateModifications = false;

    @Override
    public boolean isLogQueriesEnabled() {
        return getSourceTarget().isLogQueriesEnabled();
    }

    @Override
    public boolean isConnectedToDatabase() {
        return false;
    }

    protected abstract QueryTarget getSourceTarget();

    @Override
    public <E extends Entity> void refresh(Class<E> entityClazz) {
        try {
            if (getSourceTarget() == null) throw new SourceTargetUnderBufferedTargetMissed(this.getClass());
            Entities<E> cached = (Entities<E>) refreshQueries.get(entityClazz).findMany();
            getStorage().put(entityClazz, cached);
        } catch (Exception e) {
            throw new DaobabEntityCreationException(entityClazz, e);
        }
    }

    @Override
    public <E extends Entity> Entities<E> getEntities(E entity) {
        Entities<E> cached = (Entities<E>) getStorage().get(entity.getClass());
        if (cached == null) {
            if (!getStorage().containsKey(entity.getClass())) {
                throw new EntityNotRegisteredIntoBuffer(entity.getClass(), this.getClass());
            }
            try {
                if (getSourceTarget() == null) throw new SourceTargetUnderBufferedTargetMissed(this.getClass());
                cached = getSourceTarget().findAll(entity);
            } catch (Exception e) {
                throw new DaobabEntityCreationException(entity.getClass(), e);
            }
            getStorage().put(entity.getClass(), cached);
        }
        return cached;
    }


    @Override
    public <E extends Entity> Entities<E> getEntities(Class<E> entityClazz) {
        Entities<E> cached = (Entities<E>) getStorage().get(entityClazz);
        if (cached == null) {
            if (!getStorage().containsKey(entityClazz)) {
                throw new EntityNotRegisteredIntoBuffer(entityClazz, this.getClass());
            }
            try {
                if (getSourceTarget() == null) throw new SourceTargetUnderBufferedTargetMissed(this.getClass());
                cached = getSourceTarget().findAll(entityClazz.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                throw new DaobabEntityCreationException(entityClazz, e);
            }
            getStorage().put(entityClazz, cached);
        }
        return cached;
    }

    public <E extends Entity> E insert(QueryInsert<E> query, boolean transaction) {
        if (isPropagateModifications()) {
            if (getSourceTarget() == null) throw new SourceTargetUnderBufferedTargetMissed(this.getClass());
            getSourceTarget().insert(query, transaction);
        }
        return super.insert(query, transaction);
    }

    public <E extends Entity> E insert(QueryInsert<E> query, Propagation propagation) {
        if (!(this instanceof TransactionalTarget)) throw new TargetUntransactional(this);
        if (isPropagateModifications()) {
            if (getSourceTarget() == null) throw new SourceTargetUnderBufferedTargetMissed(this.getClass());
            handleTransactionalTarget((TransactionalTarget) this, propagation, (target, transaction) -> target.insert(query, transaction));
        }
        return super.insert(query, propagation);
    }


    public <E extends Entity> int update(QueryUpdate<E> query, boolean transaction) {
        if (isPropagateModifications()) {
            if (getSourceTarget() == null) throw new SourceTargetUnderBufferedTargetMissed(this.getClass());
            getSourceTarget().update(query, transaction);
        }
        return super.update(query, transaction);
    }

    public <E extends Entity> int update(QueryUpdate<E> query, Propagation propagation) {
        if (isPropagateModifications()) {
            if (getSourceTarget() == null) throw new SourceTargetUnderBufferedTargetMissed(this.getClass());
            handleTransactionalTarget((TransactionalTarget) this, propagation, (target, transaction) -> target.update(query, transaction));
        }
        return super.update(query, propagation);
    }

    public <E extends Entity> int delete(QueryDelete<E> query, boolean transaction) {
        if (isPropagateModifications()) {
            if (getSourceTarget() == null) throw new SourceTargetUnderBufferedTargetMissed(this.getClass());
            getSourceTarget().delete(query, transaction);
        }
        return super.delete(query, transaction);
    }

    public <E extends Entity> int delete(QueryDelete<E> query, Propagation propagation) {
        if (isPropagateModifications()) {
            if (getSourceTarget() == null) throw new SourceTargetUnderBufferedTargetMissed(this.getClass());
            handleTransactionalTarget((TransactionalTarget) this, propagation, (target, transaction) -> target.delete(query, transaction));
        }
        return super.delete(query, propagation);
    }

    @Override
    public List<Entity> getTables() {
        if (getSourceTarget() == null) throw new SourceTargetUnderBufferedTargetMissed(this.getClass());
        return getSourceTarget().getTables();
    }

//    @Override
//    public void refreshAll() {
//        getStorage().keySet().forEach(this::refresh);
//    }

//    @Override
//    public <E extends Entity> void setRefreshQuery(QueryEntity<E> refreshQuery) {
//        if (refreshQuery == null) return;
//        Class<E> entityClass = refreshQuery.getEntityClass();
//        refreshQueries.put(entityClass, refreshQuery);
//    }

//    public <E extends Entity> void register(QueryEntity<E> refreshQuery) {
//        if (refreshQuery == null) return;
//        refreshQueries.put(refreshQuery.getEntityClass(), refreshQuery);
//        getStorage().put(refreshQuery.getEntityClass(), refreshQuery.findMany());
//    }

    public boolean isPropagateModifications() {
        return propagateModifications;
    }

    public void setPropagateModifications(boolean propagateModifications) {
        this.propagateModifications = propagateModifications;
    }
}
