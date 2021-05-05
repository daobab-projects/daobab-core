package io.daobab.model;

import io.daobab.target.QueryReceiver;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public interface Entity extends DaobabDto, ColumnsProvider {

    String getEntityName();

//    <X> X getColumnParam(String key);
//
//    <X> void setColumnParam(String key, X param);

    default Class<? extends Entity> getEntityClass() {
        return this.getClass();
    }

    default void beforeInsert(QueryReceiver target) {
    }

    default void beforeUpdate(QueryReceiver target) {
    }

    default void beforeDelete(QueryReceiver target) {
    }

    default void afterSelect(QueryReceiver target) {
    }

    default void afterInsert(QueryReceiver target) {
    }

    default void afterUpdate(QueryReceiver target) {
    }

    default void afterDelete(QueryReceiver target) {
    }


}
