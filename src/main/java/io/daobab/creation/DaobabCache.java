package io.daobab.creation;

import io.daobab.error.DaobabException;
import io.daobab.error.MandatoryEntity;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.Table;
import io.daobab.model.TableColumn;

import java.util.*;
import java.util.function.Supplier;

@SuppressWarnings({"rawtypes", "java:S6548"})
public class DaobabCache {

    private static final DaobabCache INSTANCE = new DaobabCache();
    private final Map<String, Column> columnCache;
    private final Map<Class<? extends Entity>, List<TableColumn>> entityTableCache;

    private DaobabCache() {
        columnCache = new HashMap<>();
        entityTableCache = new HashMap<>();
    }

    public static <E extends Table<?>, F> Column getColumn(String fieldName, String columnName, E entity, Class<F> clazz) {

        return INSTANCE.columnCache.computeIfAbsent(entity.entityClass().getName() + fieldName,
                x -> {
                    if (clazz.isAssignableFrom(Optional.class) || clazz.isAssignableFrom(Collection.class)) {
                        throw new DaobabException("Collections, Arrays and Optionals has to provide innerTypeClass as well");
                    }
                    return ColumnCreator.createColumn(fieldName, columnName, entity, clazz);
                });
    }

    public static <E extends Table<?>, F> Column getColumn(String fieldName, String columnName, E entity, Class<F> clazz, Class innerTypeClazz) {
        return INSTANCE.columnCache.computeIfAbsent(entity.entityClass().getName() + fieldName,
                x -> ColumnCreator.createInnerTypeColumn(fieldName, columnName, entity, clazz, innerTypeClazz));
    }

    /**
     * Get List o selected entity TableColumns
     * An internal cache is used - a map with entity class as a key and list of TableColumn as a value.
     * If the map does not contain the provided entity, a new entry is being computed with a list delivered by the supplier.
     *
     * @param entity
     * @param tableColumnsSupplier
     * @param <E>
     * @return
     */
    public static <E extends Table<?>> List<TableColumn> getTableColumns(E entity, Supplier<List<TableColumn>> tableColumnsSupplier) {
        if (entity == null) {
            throw new MandatoryEntity();
        }
        return INSTANCE.entityTableCache.computeIfAbsent(entity.entityClass(), x -> tableColumnsSupplier.get());

    }
}
