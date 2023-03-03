package io.daobab.target.database.converter;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.PrimaryKey;
import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.converter.standard.*;
import io.daobab.target.database.converter.type.DatabaseTypeConverter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DatabaseConverterManager {

    private final DataBaseTarget target;

    private final Map<String, Optional<DatabaseTypeConverter<?, ?>>> cache = new HashMap<>();

    private final Map<String, DatabaseTypeConverter<?, ?>> columnConverters = new HashMap<>();
    private final Map<Class<?>, DatabaseTypeConverter<?, ?>> typeConverters = new HashMap<>();


    @SuppressWarnings({"rawtypes", "unchecked"})
    public DatabaseConverterManager(DataBaseTarget target) {
        this.target = target;
        registerTypeConverter(BigDecimal.class, new StandardTypeConverterBigDecimal());
        registerTypeConverter(BigInteger.class, new StandardTypeConverterBigInteger());
        registerTypeConverter(Boolean.class, new StandardTypeConverterBoolean());
        registerTypeConverter(byte[].class, new StandardTypeConverterBytes());
        registerTypeConverter(Double.class, new StandardTypeConverterDouble());
        registerTypeConverter(Float.class, new StandardTypeConverterFloat());
        registerTypeConverter(Integer.class, new StandardTypeConverterInteger());
        registerTypeConverter(Long.class, new StandardTypeConverterLong());
        registerTypeConverter(java.sql.Date.class, new StandardTypeConverterSqlDate(target));
        registerTypeConverter(String.class, new StandardTypeConverterString());
        registerTypeConverter(Time.class, new StandardTypeConverterTime(target));
        registerTypeConverter(Timestamp.class, new StandardTypeConverterTimestamp(target));
        registerTypeConverter(java.util.Date.class, new StandardTypeConverterUtilDate(target));

        for (Entity entity : target.getTables()) {
            if (entity instanceof PrimaryKey) {
                Class pkFieldType = ((PrimaryKey) entity).colID().getFieldClass();

                registerTypeConverter(entity.getClass(), new StandardTypeConverterPrimaryKeyEntity(target, typeConverters.get(pkFieldType), entity));


            }
        }

//        for (Entity entity : target.getTables()) {
//            if (entity instanceof PrimaryKey) {
//                Class pkFieldType = ((PrimaryKey) entity).colID().getFieldClass();
//                if (Entities.class.isAssignableFrom(pkFieldType)) {
//                    System.out.println(">>> "+pkFieldType.getName());
//                    registerTypeConverter(entity.getClass(), new StandardTypeConverterPrimaryKeyEntityList(target, typeConverters.get(pkFieldType), entity));
//                } else {
//                    registerTypeConverter(entity.getClass(), new StandardTypeConverterPrimaryKeyEntity(target, typeConverters.get(pkFieldType), entity));
//                }
//
//            }
//        }

    }

    private DataBaseTarget getTarget() {
        return target;
    }

    public Optional<DatabaseTypeConverter<?, ?>> getConverter(Column column) {
        return cache.computeIfAbsent(column.getEntityName() + column.getFieldName(), tableColumn -> {

            DatabaseTypeConverter<?, ?> rv = columnConverters.get(tableColumn);
            if (rv == null) {
                if (column.getColumnName() == null) {
                    if (List.class.isAssignableFrom(column.getFieldClass())) {
                        rv = new StandardTypeConverterVoid(getTarget());
                    } else {
                        rv = new StandardTypeConverterVoid(getTarget());
                    }

                } else {
                    rv = typeConverters.get(column.getFieldClass());
                }
            }
            System.out.println(">>> column: " + column + " type: " + column.getFieldClass());
            if (rv == null && List.class.isAssignableFrom(column.getFieldClass())) {
                DatabaseTypeConverter<?, ?> entityConvert = typeConverters.get(column.getInstance().getClass());
                if (entityConvert instanceof StandardTypeConverterPrimaryKeyEntity) {
                    System.out.println(">>> standart type converter list for " + column);
                    StandardTypeConverterPrimaryKeyEntity<?, ?> standardTypeConverterPrimaryKeyEntity = (StandardTypeConverterPrimaryKeyEntity<?, ?>) entityConvert;
                    rv = standardTypeConverterPrimaryKeyEntity.toMany();

                }
            }
            if (rv == null && column.getFieldClass().isEnum()) {
                rv = new StandardTypeConverterEnum(column.getFieldClass());
            }
            System.out.println(">>> typeconverter: " + rv.getClass().getName());


            return Optional.ofNullable(rv);
        });
    }


    public <F> DatabaseConverterManager setColumnConverter(Column<?, F, ?> column, DatabaseTypeConverter<F, ?> typeConverter) {
        columnConverters.put(column.getEntityName() + column.getFieldName(), typeConverter);
        cache.put(column.getEntityName() + column.getFieldName(), Optional.ofNullable(typeConverter));
        return this;
    }

    public <F> DatabaseConverterManager registerTypeConverter(Class<?> type, DatabaseTypeConverter<F, ?> typeConverter) {
        typeConverters.put(type, typeConverter);
        cache.clear();
        return this;
    }

    public void clear() {
        columnConverters.clear();
        typeConverters.clear();
        cache.clear();
    }
}
