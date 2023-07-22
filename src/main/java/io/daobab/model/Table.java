package io.daobab.model;

import io.daobab.converter.json.JsonConverterManager;
import io.daobab.converter.json.JsonProvider;
import io.daobab.creation.EntityBuilder;
import io.daobab.creation.EntityCreator;
import io.daobab.target.QueryHandler;
import io.daobab.target.Target;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class Table<E extends Table> implements Entity, MapHandler<E>, JsonProvider {

    private final Map<String, Object> dtoParameterMap;

    protected Table() {
        this(new HashMap<>());
    }

    protected Table(Map<String, Object> dtoParameterMap) {
        this.dtoParameterMap = dtoParameterMap;
    }

    @Override
    public String toJson() {
        return JsonConverterManager.INSTANCE.getEntityJsonConverter(this).toJson(new StringBuilder(), this).toString();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <X> X readParam(String key) {
        return (X) dtoParameterMap.get(key);
    }

    @Override
    public <X> E storeParam(String key, X param) {
        Map<String, Object> newParameters = new HashMap<>(dtoParameterMap);
        newParameters.put(key, param);
        return (E) EntityCreator.createEntity(entityClass(), newParameters);
    }

    @Override
    public Class<? extends Entity> entityClass() {
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
        Map<String, Object> params = new HashMap<>(dtoParameterMap);
        params.put(key, value);
        return (E) EntityCreator.createEntity(entityClass(), params);
    }

    public E putAll(Map<String, Object> values) {
        Map<String, Object> params = new HashMap<>(dtoParameterMap);
        params.putAll(values);
        return (E) EntityCreator.createEntity(entityClass(), params);
    }

    public E merge(Table<?> anotherTable) {
        Map<String, Object> params = new HashMap<>(dtoParameterMap);
        params.putAll(anotherTable.accessParameterMap());
        return (E) EntityCreator.createEntity(entityClass(), params);
    }

    public EntityBuilder<E> builder() {
        EntityBuilder<E> builder = (EntityBuilder<E>) new EntityBuilder<>(entityClass());
        builder.addAll(this);
        return builder;
    }


    @Override
    public Map<String, Object> accessParameterMap() {
        return dtoParameterMap;
    }

}
