package io.daobab.clone;

import io.daobab.model.Column;
import io.daobab.model.Entity;

import java.util.HashMap;
import java.util.Map;

public class EntityBuilder<E extends Entity> {

    private final Class<E> clazz;

    private final Map<String, Object> params;

    public EntityBuilder(Class<E> clazz) {
        this(clazz, new HashMap<>());
    }

    public EntityBuilder(Class<E> clazz, Map<String, Object> params) {
        this.clazz = clazz;
        this.params = params;
    }

    public <F> EntityBuilder<E> add(Column<E, F, ?> column, F value) {
        params.put(column.getFieldName(), value);
        return this;
    }

    public E build() {
        return EntityDuplicator.createEntity(clazz, params);
    }
}
