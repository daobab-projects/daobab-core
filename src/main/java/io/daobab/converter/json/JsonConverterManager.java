package io.daobab.converter.json;

import io.daobab.converter.JsonConverter;
import io.daobab.model.Field;
import io.daobab.target.database.converter.standard.*;
import io.daobab.target.database.converter.type.DatabaseTypeConverter;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class JsonConverterManager {

    private final Map<String, Optional<JsonConverter<?>>> cache = new HashMap<>();
    private final Map<Field<?,?,?>, JsonConverter<?>> fieldConverters = new HashMap<>();
    private final Map<Class<?>, JsonConverter<?>> typeConverters = new HashMap<>();

    @SuppressWarnings({"rawtypes", "unchecked"})
    public JsonConverterManager() {
        registerTypeConverter(BigDecimal.class, new JsonBigDecimalConverter());
        registerTypeConverter(BigInteger.class, new JsonBigIntegerConverter());
        registerTypeConverter(Boolean.class, new JsonBooleanConverter());
        registerTypeConverter(boolean.class, new JsonBooleanConverter());
//        registerTypeConverter(byte[].class, new StandardTypeConverterBytes());
        registerTypeConverter(Double.class, new JsonDoubleConverter());
        registerTypeConverter(double.class, new JsonDoubleConverter());
        registerTypeConverter(Float.class, new JsonFloatConverter());
        registerTypeConverter(float.class, new JsonFloatConverter());
        registerTypeConverter(Integer.class, new JsonIntegerConverter());
        registerTypeConverter(int.class, new JsonIntegerConverter());
        registerTypeConverter(Byte.class, new JsonByteConverter());
        registerTypeConverter(byte.class, new JsonByteConverter());
        registerTypeConverter(Short.class, new JsonShortConverter());
        registerTypeConverter(short.class, new JsonShortConverter());
        registerTypeConverter(Long.class, new JsonLongConverter());
        registerTypeConverter(long.class, new JsonLongConverter());
//        registerTypeConverter(java.sql.Date.class, new StandardTypeConverterSqlDate(target));
        registerTypeConverter(String.class, new JsonStringConverter());
//        registerTypeConverter(Time.class, new StandardTypeConverterTime(target));
//        registerTypeConverter(Timestamp.class, new StandardTypeConverterTimestamp(target));
//        registerTypeConverter(java.util.Date.class, new StandardTypeConverterUtilDate(target));
//        registerTypeConverter(LocalDate.class, new StandardTypeConverterLocalDate(target));
//        registerTypeConverter(LocalDateTime.class, new StandardTypeConverterLocalDateTime(target));
//        registerTypeConverter(ZonedDateTime.class, new StandardTypeConverterZonedDateTime(target));
//        registerTypeConverter(LocalTime.class, new StandardTypeConverterLocalTime(target));
//        registerTypeConverter(URL.class, new StandardTypeConverterURL());

//        for (Entity entity : target.getTables()) {
//            if (entity instanceof PrimaryKey) {
//                Class pkFieldType = ((PrimaryKey) entity).colID().getFieldClass();
//                registerTypeConverter(entity.getClass(), new StandardTypeConverterPrimaryKeyEntity(target, typeConverters.get(pkFieldType), entity));
//            }
//        }
    }


    @SuppressWarnings({"java:S1452", "java:S3776"})
    public Optional<JsonConverter<?>> getConverter(Field<?,?,?> field) {
        JsonConverter<?> jc= fieldConverters.computeIfAbsent(field, f ->typeConverters.get(f.getFieldClass()));
        return Optional.ofNullable(jc);
    }


    public <F> JsonConverterManager registerTypeConverter(Class<F> type, JsonConverter<F> typeConverter) {
        typeConverters.put(type, typeConverter);
        cache.clear();
        return this;
    }

    public void clear() {
        fieldConverters.clear();
        typeConverters.clear();
        cache.clear();
    }
}
