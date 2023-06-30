package io.daobab.model;

import io.daobab.converter.JsonProvider;
import io.daobab.target.QueryHandler;
import io.daobab.target.Target;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface Entity extends ColumnsProvider, JsonProvider {


    Class<? extends Entity> getEntityClass();

    <T extends Target & QueryHandler> void beforeInsert(T target);

    <T extends Target & QueryHandler> void beforeUpdate(T target);

    <T extends Target & QueryHandler> void beforeDelete(T target);

    <T extends Target & QueryHandler> void afterSelect(T target);

    <T extends Target & QueryHandler> void afterInsert(T target);

    <T extends Target & QueryHandler> void afterUpdate(T target);

    <T extends Target & QueryHandler> void afterDelete(T target);


}
