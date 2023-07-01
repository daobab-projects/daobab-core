package io.daobab.creation;

import io.daobab.model.Column;
import io.daobab.model.Table;

import java.util.HashMap;

public class ColumnCache extends HashMap<String, Column> {

    public static final ColumnCache INSTANCE = new ColumnCache();

    public <E extends Table<?>, F> Column getColumn(String fieldName, String columnName, E entity, Class<F> clazz) {
        return computeIfAbsent(entity.entityClass().getName() + fieldName,
                x -> ColumnCreator.createColumn(fieldName, columnName, entity, clazz));

    }
}
