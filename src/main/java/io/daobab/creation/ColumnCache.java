package io.daobab.creation;

import io.daobab.error.DaobabException;
import io.daobab.model.Column;
import io.daobab.model.Table;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

@SuppressWarnings({"rawtypes", "java:S6548"})
public class ColumnCache extends HashMap<String, Column> {

    private ColumnCache() {
    }

    public static final ColumnCache INSTANCE = new ColumnCache();


    public <E extends Table<?>, F> Column getColumn(String fieldName, String columnName, E entity, Class<F> clazz) {

        return computeIfAbsent(entity.entityClass().getName() + fieldName,
                x -> {
                    if (clazz.isAssignableFrom(Optional.class) || clazz.isAssignableFrom(Collection.class)) {
                        throw new DaobabException("Collections, Arrays and Optionals has to provide innerTypeClass as well");
                    }
                    return ColumnCreator.createColumn(fieldName, columnName, entity, clazz);
                });
    }

    public <E extends Table<?>, F> Column getColumn(String fieldName, String columnName, E entity, Class<F> clazz, Class innerTypeClazz) {
        return computeIfAbsent(entity.entityClass().getName() + fieldName,
                x -> ColumnCreator.createInnerTypeColumn(fieldName, columnName, entity, clazz, innerTypeClazz));
    }
}
