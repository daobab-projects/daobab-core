package io.daobab.model;

import io.daobab.target.QueryHandler;
import io.daobab.target.Target;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public interface Entity extends ColumnsProvider {

    String getEntityName();

    default Class<? extends Entity> getEntityClass() {
        return this.getClass();
    }

    default <T extends Target & QueryHandler> void beforeInsert(T target) {
    }

    default <T extends Target & QueryHandler> void beforeUpdate(T target) {
    }

    default <T extends Target & QueryHandler> void beforeDelete(T target) {
    }

    default <T extends Target & QueryHandler> void afterSelect(T target) {
    }

    default <T extends Target & QueryHandler> void afterInsert(T target) {
    }

    default <T extends Target & QueryHandler> void afterUpdate(T target) {
    }

    default <T extends Target & QueryHandler> void afterDelete(T target) {
    }


}
