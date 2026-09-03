package io.daobab.converter.json;

import io.daobab.converter.json.conversion.EntityJsonConversion;
import io.daobab.converter.json.conversion.FieldJsonConversion;
import io.daobab.converter.json.conversion.PlateJsonConversion;
import io.daobab.converter.json.type.*;
import io.daobab.error.DaobabException;
import io.daobab.model.Entity;
import io.daobab.model.Field;
import io.daobab.model.Plate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * The registry and resolver of the JSON {@link JsonConverter}s. The singleton {@link #INSTANCE} maps every
 * supported Java type to its converter (registered in the constructor, extensible via
 * {@link #registerTypeConverter(Class, JsonConverter)}), and resolves the converter for a queried {@code Field} -
 * unwrapping {@code Optional}/{@code List} inner types, treating enums by name and daobab entities through their
 * own {@code toJson}. Resolved lookups are cached. It is the JSON-wire counterpart of the database
 * {@code DatabaseConverterManager}.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings({"java:S6548", "rawtypes"})
public class JsonConverterManager {

    private final Map<Field<?, ?, ?>, JsonConverter<?>> fieldConverters = new ConcurrentHashMap<>();
    private final Map<Field<?, ?, ?>, FieldJsonConversion<?>> fieldJsonConversions = new ConcurrentHashMap<>();
    //keyed by the plate's field list (its shape), not by the plate itself: a plate is a mutable row of data,
    //so keying by the row would grow the cache without a bound and let a key change its own hash code
    private final Map<List<Field>, PlateJsonConversion> plateJsonConversions = new ConcurrentHashMap<>();
    //keyed by the entity class, not by the entity instance, for the same reason
    private final Map<Class<? extends Entity>, EntityJsonConversion> entityJsonConversions = new ConcurrentHashMap<>();
    private final Map<Class<?>, JsonConverter<?>> typeConverters = new ConcurrentHashMap<>();

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
        registerTypeConverter(Instant.class, new JsonInstantConverter());
        registerTypeConverter(LocalTime.class, new JsonLocalTimeConverter());
        registerTypeConverter(URL.class, new JsonUrlConverter());
        registerTypeConverter(URI.class, new JsonUriConverter());
        registerTypeConverter(UUID.class, new JsonUuidConverter());
        registerTypeConverter(Locale.class, new JsonLocaleConverter());
        registerTypeConverter(Character.class, new JsonCharacterConverter());
        registerTypeConverter(char.class, new JsonCharacterConverter());
        registerTypeConverter(OffsetDateTime.class, new JsonOffsetDateTimeConverter());
        registerTypeConverter(OffsetTime.class, new JsonOffsetTimeConverter());
        registerTypeConverter(YearMonth.class, new JsonYearMonthConverter());
        registerTypeConverter(MonthDay.class, new JsonMonthDayConverter());
        registerTypeConverter(Duration.class, new JsonDurationConverter());
        registerTypeConverter(Period.class, new JsonPeriodConverter());
        //parity with the DB type converters in io.daobab.target.database.converter.type
        registerTypeConverter(java.sql.Array.class, new JsonArrayConverter());
        registerTypeConverter(SQLXML.class, new JsonSqlXmlConverter());
        registerTypeConverter(Void.class, new JsonVoidConverter());
        //additional commonly-used value types
        registerTypeConverter(ZoneId.class, new JsonZoneIdConverter());
        registerTypeConverter(Currency.class, new JsonCurrencyConverter());

//        for (Entity entity : target.getTables()) {
//            if (entity instanceof PrimaryKey) {
//                Class pkFieldType = ((PrimaryKey) entity).colID().getFieldClass();
//                registerTypeConverter(entity.getClass(), new StandardTypeConverterPrimaryKeyEntity(target, typeConverters.get(pkFieldType), entity));
//            }
//        }
    }


    @SuppressWarnings({"java:S1452", "unchecked"})
    public Optional<JsonConverter<?>> getConverter(Field<?, ?, ?> field) {
        JsonConverter<?> jc = fieldConverters.computeIfAbsent(field, fld -> {
            Class fieldClass = fld.getFieldClass();

            if (Optional.class.isAssignableFrom(fieldClass)) {
                return wrapInnerConverter(fld, JsonOptionalConverter::new);
            }

            if (List.class.isAssignableFrom(fieldClass)) {
                return wrapInnerConverter(fld, JsonListConverter::new);
            }

            if (Set.class.isAssignableFrom(fieldClass)) {
                return wrapInnerConverter(fld, JsonSetConverter::new);
            }

            if (fieldClass.isEnum()) {
                return new JsonEnumConverter(fieldClass);
            } else if (Entity.class.isAssignableFrom(fieldClass)) {
                return new JsonDaobabEntityConverter(fieldClass);
            } else {
                return typeConverters.get(fieldClass);
            }
        });
        return Optional.ofNullable(jc);
    }

    /**
     * Resolves the converter of a wrapper field's inner type ({@code Optional}, {@code List}, {@code Set}) and
     * wraps it with the matching collection/optional converter.
     */
    private JsonConverter<?> wrapInnerConverter(Field<?, ?, ?> field, Function<JsonConverter<?>, JsonConverter<?>> wrapper) {
        Class<?> innerFieldClass = field.getInnerTypeClass();
        if (innerFieldClass == null) {
            throw new DaobabException("InnerFieldClass has to be provided for a field " + field);
        }
        return getTypeConverter(innerFieldClass)
                .map(wrapper)
                .orElseThrow(() -> new DaobabException("Cannot find a converter for the inner type %s", innerFieldClass));
    }

    @SuppressWarnings({"java:S1452", "unchecked"})
    public Optional<JsonConverter<?>> getTypeConverter(Class<?> clazz) {
        JsonConverter<?> registered = typeConverters.get(clazz);
        if (registered != null) {
            return Optional.of(registered);
        }
        JsonConverter<?> jc = null;
        if (clazz.isEnum()) {
            jc = new JsonEnumConverter(clazz);
        } else if (Entity.class.isAssignableFrom(clazz)) {
            jc = new JsonDaobabEntityConverter(clazz);
        }
        if (jc != null) {
            typeConverters.put(clazz, jc);
        }
        return Optional.ofNullable(jc);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <E extends Entity> EntityJsonConversion<E> getEntityJsonConverter(E entity) {
        return entityJsonConversions.computeIfAbsent(entity.entityClass(), c -> new EntityJsonConversion(entity, this));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <F> FieldJsonConversion<F> getFieldJsonConverter(Field<?, F, ?> field) {
        return (FieldJsonConversion<F>) fieldJsonConversions.computeIfAbsent(field, f -> new FieldJsonConversion(f, this));
    }

    @SuppressWarnings("rawtypes")
    public PlateJsonConversion getPlateJsonConverter(Plate plate) {
        List<Field> fields = plate.fields();
        if (fields == null) {
            throw new DaobabException("Cannot convert a plate carrying no fields into JSON");
        }
        return plateJsonConversions.computeIfAbsent(fields, f -> new PlateJsonConversion(f, this));
    }

    public <F> JsonConverterManager registerTypeConverter(Class<F> type, JsonConverter<F> typeConverter) {
        typeConverters.put(type, typeConverter);
        clearResolved();
        return this;
    }

    public void clear() {
        typeConverters.clear();
        clearResolved();
    }

    /**
     * Drops everything resolved from the registered type converters, so the next lookup rebuilds it.
     */
    private void clearResolved() {
        fieldConverters.clear();
        fieldJsonConversions.clear();
        plateJsonConversions.clear();
        entityJsonConversions.clear();
    }


}
