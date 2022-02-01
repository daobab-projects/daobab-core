package io.daobab.target.multi;

import io.daobab.error.DaobabEntityCreationException;
import io.daobab.model.Entity;
import io.daobab.query.QueryEntity;
import io.daobab.result.Entities;

import java.util.HashMap;
import java.util.Map;

public class QueryMultiEntityTarget extends MultiEntityTarget implements QueryMultiEntity {

    protected final Map<Class<? extends Entity>, QueryEntity<? extends Entity>> refreshQueries = new HashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Entity> void refresh(Class<E> entityClazz) {
        try {
            Entities<E> cached = (Entities<E>) refreshQueries.get(entityClazz).findMany();
            getStorage().put(entityClazz, cached);
        } catch (Exception e) {
            throw new DaobabEntityCreationException(entityClazz, e);
        }
    }

    public <E extends Entity> void register(QueryEntity<E> refreshQuery) {
        if (refreshQuery == null) return;
        refreshQueries.put(refreshQuery.getEntityClass(), refreshQuery);
        getStorage().put(refreshQuery.getEntityClass(), refreshQuery.findMany());
    }

    @Override
    public void refreshAll() {
        getStorage().keySet().forEach(this::refresh);
    }


}
