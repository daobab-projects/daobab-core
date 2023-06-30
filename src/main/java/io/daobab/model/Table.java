package io.daobab.model;

import io.daobab.clone.EntityDuplicator;
import io.daobab.converter.JsonProvider;
import io.daobab.converter.json.JsonConverterManager;
import io.daobab.target.QueryHandler;
import io.daobab.target.Target;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class Table<E extends Table> implements EntityMap<E>, JsonProvider {

    private final Map<String, Object> parameters;

    protected Table() {
        this(new HashMap<>());
    }

    protected Table(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toJson() {
        return JsonConverterManager.INSTANCE.getEntityJsonConverter(this).toJson(new StringBuilder(), this).toString();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <X> X getColumnParam(String key) {
        return (X) parameters.get(key);
    }

    //    @Override
    public <X> E setColumnParam(String key, X param) {
        Map<String, Object> newParameters = new HashMap<>(parameters);
        newParameters.put(key, param);
        return (E) EntityDuplicator.createEntity(getEntityClass(), newParameters);
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

    public E put(String key, Object value) {
        Map<String, Object> params = new HashMap<>(parameters);
        params.put(key, value);
        return (E) EntityDuplicator.createEntity(getEntityClass(), params);
    }

    public E putAll(Map<String, Object> values) {
        Map<String, Object> params = new HashMap<>(parameters);
        params.putAll(values);
        return (E) EntityDuplicator.createEntity(getEntityClass(), params);
    }

    public E merge(Table<?> anotherTable) {
        Map<String, Object> params = new HashMap<>(parameters);
        params.putAll(anotherTable.getParameters());
        return (E) EntityDuplicator.createEntity(getEntityClass(), params);
    }


    public Map<String, Object> getParameters() {
        return parameters;
    }


}
