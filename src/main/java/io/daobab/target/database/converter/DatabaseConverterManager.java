package io.daobab.target.database.converter;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.PrimaryKey;
import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.converter.standard.*;
import io.daobab.target.database.converter.type.DatabaseTypeConverter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
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
        registerTypeConverter(boolean.class, new StandardTypeConverterBoolean());
        registerTypeConverter(byte[].class, new StandardTypeConverterBytes());
        registerTypeConverter(Double.class, new StandardTypeConverterDouble());
        registerTypeConverter(double.class, new StandardTypeConverterDouble());
        registerTypeConverter(Float.class, new StandardTypeConverterFloat());
        registerTypeConverter(float.class, new StandardTypeConverterFloat());
        registerTypeConverter(Integer.class, new StandardTypeConverterInteger());
        registerTypeConverter(int.class, new StandardTypeConverterInteger());
        registerTypeConverter(Byte.class, new StandardTypeConverterByte());
        registerTypeConverter(byte.class, new StandardTypeConverterByte());
        registerTypeConverter(Short.class, new StandardTypeConverterShort());
        registerTypeConverter(short.class, new StandardTypeConverterShort());
        registerTypeConverter(Long.class, new StandardTypeConverterLong());
        registerTypeConverter(long.class, new StandardTypeConverterLong());
        registerTypeConverter(java.sql.Date.class, new StandardTypeConverterSqlDate(target));
        registerTypeConverter(String.class, new StandardTypeConverterString());
        registerTypeConverter(Time.class, new StandardTypeConverterTime(target));
        registerTypeConverter(Timestamp.class, new StandardTypeConverterTimestamp(target));
        registerTypeConverter(java.util.Date.class, new StandardTypeConverterUtilDate(target));
        registerTypeConverter(LocalDate.class, new StandardTypeConverterLocalDate(target));
        registerTypeConverter(LocalDateTime.class, new StandardTypeConverterLocalDateTime(target));
        registerTypeConverter(ZonedDateTime.class, new StandardTypeConverterZonedDateTime(target));
        registerTypeConverter(LocalTime.class, new StandardTypeConverterLocalTime(target));
        registerTypeConverter(URL.class, new StandardTypeConverterURL());

        for (Entity entity : target.getTables()) {
            if (entity instanceof PrimaryKey) {
                Class pkFieldType = ((PrimaryKey) entity).colID().getFieldClass();
                registerTypeConverter(entity.getClass(), new StandardTypeConverterPrimaryKeyEntity(target, typeConverters.get(pkFieldType), entity));
            }
        }
    }

    private DataBaseTarget getTarget() {
        return target;
    }

    @SuppressWarnings({"java:S1452", "java:S3776"})
    public Optional<DatabaseTypeConverter<?, ?>> getConverter(Column<?, ?, ?> column) {
        return cache.computeIfAbsent(target.getEntityName(column.getEntityClass()) + column.getFieldName(), tableColumn -> {

            DatabaseTypeConverter<?, ?> rv = columnConverters.get(tableColumn);
            if (rv == null) {
                if (column.getColumnName() == null) {
                    if (List.class.isAssignableFrom(column.getFieldClass())) {
                        //TODO: list?
                        rv = new StandardTypeConverterVoid(getTarget());
                    } else {
                        rv = new StandardTypeConverterVoid(getTarget());
                    }

                } else {
                    rv = typeConverters.get(column.getFieldClass());
                }
            }
            if (rv == null && List.class.isAssignableFrom(column.getFieldClass())) {
                DatabaseTypeConverter<?, ?> entityConvert = typeConverters.get(column.getInstance().getClass());
                if (entityConvert instanceof StandardTypeConverterPrimaryKeyEntity) {
                    StandardTypeConverterPrimaryKeyEntity<?, ?> standardTypeConverterPrimaryKeyEntity = (StandardTypeConverterPrimaryKeyEntity<?, ?>) entityConvert;
                    rv = standardTypeConverterPrimaryKeyEntity.toMany();
                }
            }
            if (rv == null && column.getFieldClass().isEnum()) {
                rv = new StandardTypeConverterEnum(column.getFieldClass());
            }

            return Optional.ofNullable(rv);
        });
    }


    public <F> DatabaseConverterManager setColumnConverter(Column<?, F, ?> column, DatabaseTypeConverter<F, ?> typeConverter) {
        columnConverters.put(target.getEntityName(column.getEntityClass()) + column.getFieldName(), typeConverter);
        cache.put(target.getEntityName(column.getEntityClass()) + column.getFieldName(), Optional.ofNullable(typeConverter));
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
