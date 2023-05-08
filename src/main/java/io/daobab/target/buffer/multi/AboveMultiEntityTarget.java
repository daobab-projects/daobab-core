package io.daobab.target.buffer.multi;

import io.daobab.error.*;
import io.daobab.model.Entity;
import io.daobab.target.QueryHandler;
import io.daobab.target.buffer.single.Entities;
import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.QueryDataBaseHandler;
import io.daobab.target.database.TransactionalTarget;
import io.daobab.target.database.query.DataBaseQueryDelete;
import io.daobab.target.database.query.DataBaseQueryInsert;
import io.daobab.target.database.query.DataBaseQueryUpdate;
import io.daobab.transaction.Propagation;
import io.daobab.transaction.TransactionIndicator;

import java.util.List;
import java.util.function.BiFunction;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class AboveMultiEntityTarget extends QueryMultiEntityTarget implements MultiEntity {

    private boolean propagateModifications = false;

    protected abstract DataBaseTarget getSourceTarget();

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Entity> Entities<E> getEntities(E entity) {
        Entities<E> entities = (Entities<E>) getStorage().get(entity.getClass());
        if (entities == null) {
            if (!getStorage().containsKey(entity.getClass())) {
                throw new EntityNotRegisteredIntoBuffer(entity.getClass(), this.getClass());
            }
            try {
                if (getSourceTarget() == null) throw new SourceTargetUnderBufferedTargetMissed(this.getClass());
                entities = getSourceTarget().findAll(entity);
            } catch (Exception e) {
                throw new DaobabEntityCreationException(entity.getClass(), e);
            }
            getStorage().put(entity.getClass(), entities);
        }
        return entities;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Entity> Entities<E> getEntities(Class<E> entityClazz) {
        Entities<E> entities = (Entities<E>) getStorage().get(entityClazz);
        if (entities == null) {
            if (!getStorage().containsKey(entityClazz)) {
                throw new EntityNotRegisteredIntoBuffer(entityClazz, this.getClass());
            }
            try {
                if (getSourceTarget() == null) throw new SourceTargetUnderBufferedTargetMissed(this.getClass());
                entities = getSourceTarget().findAll(entityClazz.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                throw new DaobabEntityCreationException(entityClazz, e);
            }
            getStorage().put(entityClazz, entities);
        }
        return entities;
    }

    //    @Override
    public <E extends Entity> E insert(DataBaseQueryInsert<E> query, boolean transaction) {
        if (isPropagateModifications()) {
            if (getSourceTarget() == null) throw new SourceTargetUnderBufferedTargetMissed(this.getClass());
            return getSourceTarget().insert(query, transaction);
        }
        return null;
    }

    //    @Override
    public <E extends Entity> E insert(DataBaseQueryInsert<E> query, Propagation propagation) {
        if (!(this instanceof TransactionalTarget)) throw new TargetNotTransactional(this);
        if (isPropagateModifications()) {
            if (getSourceTarget() == null) throw new SourceTargetUnderBufferedTargetMissed(this.getClass());
            return handleTransactionalTarget((TransactionalTarget) this, propagation, (target, transaction) -> ((QueryDataBaseHandler) target).insert(query, transaction));
        }
        return null;
    }

    //    @Override
    public <E extends Entity> int update(DataBaseQueryUpdate<E> query, boolean transaction) {
        if (isPropagateModifications()) {
            if (getSourceTarget() == null) throw new SourceTargetUnderBufferedTargetMissed(this.getClass());
            return getSourceTarget().update(query, transaction);
        }
        return 0;
    }

    //    @Override
    public <E extends Entity> int update(DataBaseQueryUpdate<E> query, Propagation propagation) {
        if (isPropagateModifications()) {
            if (getSourceTarget() == null) throw new SourceTargetUnderBufferedTargetMissed(this.getClass());
            return handleTransactionalTarget((TransactionalTarget) this, propagation, (target, transaction) -> ((QueryDataBaseHandler) target).update(query, transaction));
        }
        return 0;
    }

    //    @Override
    public <E extends Entity> int delete(DataBaseQueryDelete<E> query, boolean transaction) {
        if (isPropagateModifications()) {
            if (getSourceTarget() == null) throw new SourceTargetUnderBufferedTargetMissed(this.getClass());
            return getSourceTarget().delete(query, transaction);
        }
        return 0;
    }

    //    @Override
    public <E extends Entity> int delete(DataBaseQueryDelete<E> query, Propagation propagation) {
        if (isPropagateModifications()) {
            if (getSourceTarget() == null) throw new SourceTargetUnderBufferedTargetMissed(this.getClass());
            return handleTransactionalTarget((TransactionalTarget) this, propagation, (target, transaction) -> ((QueryDataBaseHandler) target).delete(query, transaction));
        }
        return 0;
    }

    @Override
    public List<Entity> getTables() {
        if (getSourceTarget() == null) throw new SourceTargetUnderBufferedTargetMissed(this.getClass());
        return getSourceTarget().getTables();
    }

    public boolean isPropagateModifications() {
        return propagateModifications;
    }

    public void setPropagateModifications(boolean propagateModifications) {
        this.propagateModifications = propagateModifications;
    }


    public <Y, T extends TransactionalTarget> Y handleTransactionalTarget(T target, Propagation propagation, BiFunction<QueryHandler, Boolean, Y> jobToDo) {
        TransactionIndicator indicator = propagation.mayBeProceeded(target);
        switch (indicator) {
            case EXECUTE_WITHOUT: {
                return jobToDo.apply(target, false);
            }
            case START_NEW_JUST_FOR_IT: {
                return target.wrapTransaction(t -> jobToDo.apply(t.getSourceTarget(), true));
            }
            case GO_AHEAD: {
                return jobToDo.apply(target, true);
            }
        }
        throw new DaobabException("Problem related to specific propagation and transaction");
    }


}
