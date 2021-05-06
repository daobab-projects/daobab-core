package io.daobab.target.multi;

import io.daobab.model.Entity;
import io.daobab.query.QueryEntity;

public interface QueryMultiEntity extends MultiEntity {

    <E extends Entity> void register(QueryEntity<E> refreshQuery);

    <E extends Entity> void refresh(Class<E> entityClazz);

    void refreshAll();
}
