package io.daobab.creation;

import io.daobab.error.DaobabException;
import io.daobab.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public <F> EntityBuilder<E> add(Field<E, F, ?> column, F value) {
        params.put(column.getFieldName(), value);
        return this;
    }

    public <F, R extends RelatedTo> EntityBuilder<E> addRelatedValue(Field<?, F, R> column, R relation) {
        params.put(column.getFieldName(), column.getValue(relation));
        return this;
    }

    public EntityBuilder<E> addAll(Table<?> entity) {
        params.putAll(entity.accessParameterMap());
        return this;
    }

    public void validateNullable() {
        String notNullNotProvided = EntityCreator.createEntity(clazz).columns().stream()
                .filter(TableColumn::isNullable)
                .map(TableColumn::getColumn)
                .map(Field::getFieldName)
                .filter(name -> params.get(name) == null)
                .collect(Collectors.joining(","));
        if (!notNullNotProvided.isEmpty()) {
            throw new DaobabException("Entity %s has not-null fields having null value: %s", clazz.getName(), notNullNotProvided);
        }
    }

    public E build() {
        return EntityCreator.createEntity(clazz, params);
    }

    public List<TableColumn> getEntityColumns() {
        return EntityCreator.createEntity(clazz).columns();
    }
}
