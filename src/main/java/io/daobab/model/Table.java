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
 * The base class of every Daobab entity: an <b>immutable</b> row backed by a {@code String -> Object} parameter
 * map. The shared column interfaces read and write through {@link #readParam(String)} / {@link #storeParam}, and
 * every mutation ({@code storeParam}, {@link #put}, {@link #putAll}, {@link #merge}) returns a <b>new</b> entity
 * instance rather than changing this one. Entities annotated {@link TableInformation @TableInformation} extend it
 * (or {@link DtoTable} when they carry a DTO). The seven lifecycle hooks default to no-ops.
 *
 * @param <E> the concrete entity type
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

    /**
     * This entity rendered as JSON.
     */
    @Override
    public String toJson() {
        return JsonConverterManager.INSTANCE.getEntityJsonConverter(this).toJson(new StringBuilder(), this).toString();
    }

    /** Reads the value stored under the key from the parameter map. */
    @SuppressWarnings("unchecked")
    @Override
    public <X> X readParam(String key) {
        return (X) dtoParameterMap.get(key);
    }

    /** Returns a new entity with the value stored under the key (this instance is left unchanged). */
    @SuppressWarnings("unchecked")
    @Override
    public <X> E storeParam(String key, X param) {
        Map<String, Object> newParameters = new HashMap<>(dtoParameterMap);
        newParameters.put(key, param);
        return (E) EntityCreator.createEntityFromOwnedMap(entityClass(), newParameters);
    }

    /** {@inheritDoc} */
    @Override
    public Class<? extends Entity> entityClass() {
        return this.getClass();
    }

    /** {@inheritDoc} No-op by default. */
    @Override
    public <T extends Target & QueryHandler> void beforeInsert(T target) {
    }

    /** {@inheritDoc} No-op by default. */
    @Override
    public <T extends Target & QueryHandler> void beforeUpdate(T target) {
    }

    /** {@inheritDoc} No-op by default. */
    @Override
    public <T extends Target & QueryHandler> void beforeDelete(T target) {
    }

    /** {@inheritDoc} No-op by default. */
    @Override
    public <T extends Target & QueryHandler> void afterSelect(T target) {
    }

    /** {@inheritDoc} No-op by default. */
    @Override
    public <T extends Target & QueryHandler> void afterInsert(T target) {
    }

    /** {@inheritDoc} No-op by default. */
    @Override
    public <T extends Target & QueryHandler> void afterUpdate(T target) {
    }

    /** {@inheritDoc} No-op by default. */
    @Override
    public <T extends Target & QueryHandler> void afterDelete(T target) {
    }

    /** Returns a new entity with the value put under the key (this instance is left unchanged). */
    public E put(String key, Object value) {
        Map<String, Object> params = new HashMap<>(dtoParameterMap);
        params.put(key, value);
        return (E) EntityCreator.createEntityFromOwnedMap(entityClass(), params);
    }

    /** Returns a new entity with all the given values put in (this instance is left unchanged). */
    public E putAll(Map<String, Object> values) {
        Map<String, Object> params = new HashMap<>(dtoParameterMap);
        params.putAll(values);
        return (E) EntityCreator.createEntityFromOwnedMap(entityClass(), params);
    }

    /** Returns a new entity merging this one with another table's parameters (the other one wins on clashes). */
    public E merge(Table<?> anotherTable) {
        Map<String, Object> params = new HashMap<>(dtoParameterMap);
        params.putAll(anotherTable.accessParameterMap());
        return (E) EntityCreator.createEntityFromOwnedMap(entityClass(), params);
    }

    /** A builder seeded with this entity's parameters. */
    public EntityBuilder<E> builder() {
        EntityBuilder<E> builder = (EntityBuilder<E>) new EntityBuilder<>(entityClass());
        builder.addAll(this);
        return builder;
    }


    /** {@inheritDoc} */
    @Override
    public Map<String, Object> accessParameterMap() {
        return dtoParameterMap;
    }

}
