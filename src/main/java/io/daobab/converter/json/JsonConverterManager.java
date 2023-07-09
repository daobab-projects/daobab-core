package io.daobab.converter.json;

import io.daobab.converter.JsonConverter;
import io.daobab.converter.json.conversion.EntityJsonConversion;
import io.daobab.converter.json.conversion.FieldJsonConversion;
import io.daobab.converter.json.conversion.PlateJsonConversion;
import io.daobab.converter.json.type.*;
import io.daobab.model.Entity;
import io.daobab.model.Field;
import io.daobab.model.Plate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings({"java:S6548", "rawtypes"})
public class JsonConverterManager {

    private final Map<String, Optional<JsonConverter<?>>> cache = new HashMap<>();
    private final Map<Field<?, ?, ?>, JsonConverter<?>> fieldConverters = new HashMap<>();
    private final Map<Field<?, ?, ?>, FieldJsonConversion<?>> fieldJsonConversions = new HashMap<>();
    private final Map<Plate, PlateJsonConversion> plateJsonConversions = new HashMap<>();
    private final Map<Entity, EntityJsonConversion> entityJsonConversions = new HashMap<>();
    private final Map<Class<?>, JsonConverter<?>> typeConverters = new HashMap<>();

    public static final JsonConverterManager INSTANCE = new JsonConverterManager();

    private JsonConverterManager() {
        registerTypeConverter(BigDecimal.class, new JsonBigDecimalConverter());
        registerTypeConverter(BigInteger.class, new JsonBigIntegerConverter());
        registerTypeConverter(Boolean.class, new JsonBooleanConverter());
        registerTypeConverter(boolean.class, new JsonBooleanConverter());
        registerTypeConverter(byte[].class, new JsonByteArrayConverter());
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
        registerTypeConverter(java.sql.Date.class, new JsonSqlDateConverter());
        registerTypeConverter(String.class, new JsonStringConverter());
        registerTypeConverter(Time.class, new JsonSqlTimeConverter());
        registerTypeConverter(Timestamp.class, new JsonTimestampConverter());
        registerTypeConverter(java.util.Date.class, new JsonDateConverter());
        registerTypeConverter(LocalDate.class, new JsonLocalDateConverter());
        registerTypeConverter(LocalDateTime.class, new JsonLocalDateTimeConverter());
        registerTypeConverter(Year.class, new JsonLocalYearConverter());
        registerTypeConverter(Month.class, new JsonLocalMonthConverter());
        registerTypeConverter(DayOfWeek.class, new JsonLocalDayOfWeekConverter());
        registerTypeConverter(ZonedDateTime.class, new JsonZonedDateTimeConverter());
        registerTypeConverter(LocalTime.class, new JsonLocalTimeConverter());
        registerTypeConverter(URL.class, new JsonUrlConverter());

//        for (Entity entity : target.getTables()) {
//            if (entity instanceof PrimaryKey) {
//                Class pkFieldType = ((PrimaryKey) entity).colID().getFieldClass();
//                registerTypeConverter(entity.getClass(), new StandardTypeConverterPrimaryKeyEntity(target, typeConverters.get(pkFieldType), entity));
//            }
//        }
    }


    @SuppressWarnings({"java:S1452", "java:S3776", "unchecked"})
    public Optional<JsonConverter<?>> getConverter(Field<?, ?, ?> field) {
        JsonConverter<?> jc = fieldConverters.computeIfAbsent(field, fld -> {
            Class fieldClass = fld.getFieldClass();
            if (fieldClass.isEnum()) {
                return new JsonEnumConverter(fieldClass);
            } else if (fieldClass.isAssignableFrom(Entity.class)) {
                return new JsonDaobabEntityConverter(fieldClass);
            } else {
                return typeConverters.get(fld.getFieldClass());
            }
        });
        return Optional.ofNullable(jc);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <E extends Entity> EntityJsonConversion<E> getEntityJsonConverter(E entity) {
        return entityJsonConversions.computeIfAbsent(entity, f -> new EntityJsonConversion(f, this));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <F> FieldJsonConversion<F> getFieldJsonConverter(Field<?, F, ?> entity) {
        return (FieldJsonConversion<F>) fieldJsonConversions.computeIfAbsent(entity, f -> new FieldJsonConversion(f, this));
    }

    public PlateJsonConversion getPlateJsonConverter(Plate entity) {
        return plateJsonConversions.computeIfAbsent(entity, f -> new PlateJsonConversion(f, this));
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
