package io.daobab.creation;

import io.daobab.error.DaobabException;
import io.daobab.error.MandatoryEntity;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.Table;
import io.daobab.model.TableColumn;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@SuppressWarnings({"rawtypes", "java:S6548"})
public class DaobabCache {

    private static final DaobabCache INSTANCE = new DaobabCache();

    /**
     * Columns per entity class and field name. Concurrent maps: the cache is filled lazily
     * from any thread touching a column for the first time. The nested keying avoids
     * a string concatenation on every column access.
     */
    private final Map<Class<? extends Entity>, Map<String, Column>> columnCache;
    private final Map<Class<? extends Entity>, List<TableColumn>> entityTableCache;

    private DaobabCache() {
        columnCache = new ConcurrentHashMap<>();
        entityTableCache = new ConcurrentHashMap<>();
    }

    public static <E extends Table<?>, F> Column getColumn(String fieldName, String columnName, E entity, Class<F> clazz) {

        return INSTANCE.columnsOf(entity.entityClass()).computeIfAbsent(fieldName,
                x -> {
                    if (clazz.isAssignableFrom(Optional.class) || clazz.isAssignableFrom(Collection.class)) {
                        throw new DaobabException("Collections, Arrays and Optionals has to provide innerTypeClass as well");
                    }
                    return ColumnCreator.createColumn(fieldName, columnName, entity, clazz);
                });
    }

    public static <E extends Table<?>, F> Column getColumn(String fieldName, String columnName, E entity, Class<F> clazz, Class innerTypeClazz) {
        return INSTANCE.columnsOf(entity.entityClass()).computeIfAbsent(fieldName,
                x -> ColumnCreator.createInnerTypeColumn(fieldName, columnName, entity, clazz, innerTypeClazz));
    }

    private Map<String, Column> columnsOf(Class<? extends Entity> entityClass) {
        return columnCache.computeIfAbsent(entityClass, x -> new ConcurrentHashMap<>());
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
