package io.daobab.converter;

import io.daobab.model.Column;
import io.daobab.target.Target;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ConverterManager {

    private final Target target;

    private final Map<String, Optional<TypeConverter<?>>> cache = new HashMap<>();

    private final Map<String, TypeConverter<?>> columnConverters = new HashMap<>();
    private final Map<Class<?>, TypeConverter<?>> typeConverters = new HashMap<>();


    public ConverterManager(Target target) {
        this.target = target;
    }

    private Target getTarget() {
        return target;
    }

    public Optional<TypeConverter<?>> getConverter(Column column) {
        return cache.computeIfAbsent(column.toString(), tableColumn -> {

            TypeConverter<?> rv = columnConverters.get(tableColumn);
            if (rv != null) {
                rv = typeConverters.get(column.getFieldClass());
            }

            return Optional.ofNullable(rv);
        });
    }


    public <F> ConverterManager setColumnConverter(Column<?, F, ?> column, TypeConverter<F> typeConverter) {
        columnConverters.put(column.toString(), typeConverter);
        cache.put(column.toString(), Optional.ofNullable(typeConverter));
        return this;
    }

    public <F> ConverterManager setTypeConverter(Column<?, F, ?> column, TypeConverter<F> typeConverter) {
        typeConverters.put(column.getFieldClass(), typeConverter);
        cache.clear();
        return this;
    }

    public void clear() {
        columnConverters.clear();
        typeConverters.clear();
        cache.clear();
    }
}
