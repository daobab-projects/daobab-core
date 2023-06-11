package io.daobab.model;

import io.daobab.converter.json.JsonConverterManager;
import io.daobab.target.QueryHandler;
import io.daobab.target.Target;

import java.util.HashMap;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class Table extends HashMap<String, Object> implements EntityMap {

    @Override
    public String toJson() {
        return JsonConverterManager.INSTANCE.getEntityJsonConverter(this).toJson(new StringBuilder(), this).toString();
    }

    @Override
    public Class<? extends Entity> getEntityClass() {
        return this.getClass();
    }

    @Override
    public <T extends Target & QueryHandler> void beforeInsert(T target) {
    }

    @Override
    public <T extends Target & QueryHandler> void beforeUpdate(T target) {
    }

    @Override
    public <T extends Target & QueryHandler> void beforeDelete(T target) {
    }

    @Override
    public <T extends Target & QueryHandler> void afterSelect(T target) {
    }

    @Override
    public <T extends Target & QueryHandler> void afterInsert(T target) {
    }

    @Override
    public <T extends Target & QueryHandler> void afterUpdate(T target) {
    }

    @Override
    public <T extends Target & QueryHandler> void afterDelete(T target) {
    }

}
