package io.daobab.target.buffer.multi;

import io.daobab.model.Entity;
import io.daobab.target.buffer.single.Entities;

@SuppressWarnings("unused")
public interface MultiEntity {

    <E extends Entity> Entities<E> getEntities(Class<E> entityClazz);

    <E extends Entity> Entities<E> getEntities(E entity);

    void put(Entities<? extends Entity>... entities);

    <E extends Entity> boolean isRegistered(Class<E> entityClass);

}
