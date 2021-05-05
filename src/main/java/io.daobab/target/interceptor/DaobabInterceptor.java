package io.daobab.target.interceptor;

import io.daobab.model.Entity;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public interface DaobabInterceptor {

    default void beforeInsert(Entity entity) {
    }

    default void afterInsert(Entity entity) {
    }

    default void beforeUpdate(Entity entity) {
    }

    default void afterUpdate(Entity entity) {
    }

    default void beforeDelete(Entity entity) {
    }

    default void afterDelete(Entity entity) {
    }
}
