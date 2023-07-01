package io.daobab.creation;

import io.daobab.model.Column;
import io.daobab.model.RelatedTo;
import io.daobab.model.Table;

import java.util.HashMap;

public class ColumnCache extends HashMap<String, Column> {

    public static final ColumnCache INSTANCE = new ColumnCache();

    public <E extends Table, F, R extends RelatedTo> Column getColumn(String fieldName, String columnName, E entity, Class<F> clazz) {

        return computeIfAbsent(entity.getEntityClass().getName() + fieldName,
                a -> ColumnCreator.createColumn(fieldName, columnName, entity, clazz));

    }
}
