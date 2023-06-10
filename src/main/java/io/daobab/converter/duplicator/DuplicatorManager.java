package io.daobab.converter.duplicator;

import io.daobab.converter.duplicator.duplication.EntityDuplication;
import io.daobab.converter.duplicator.duplication.FieldDuplication;
import io.daobab.converter.duplicator.type.*;
import io.daobab.converter.json.conversion.PlateJsonConversion;
import io.daobab.model.Entity;
import io.daobab.model.Field;
import io.daobab.model.Plate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.util.*;

public class DuplicatorManager {

    private final Map<String, Optional<Duplicator<?>>> cache = new HashMap<>();
    private final Map<Field<?, ?, ?>, Duplicator<?>> fieldDuplicators = new HashMap<>();
    private final Map<Field<?, ?, ?>, FieldDuplication<?>> fieldDuplications = new HashMap<>();
    private final Map<Plate, PlateJsonConversion> plateJsonConversions = new HashMap<>();
    private final Map<Entity, EntityDuplication> entityDuplications = new HashMap<>();
    private final Map<Class<?>, Duplicator<?>> typeDuplicators = new HashMap<>();

    public static final DuplicatorManager INSTANCE = new DuplicatorManager();

    private DuplicatorManager() {
        registerDuplicator(BigDecimal.class, new ImmutableDuplicator<>());
        registerDuplicator(BigInteger.class, new ImmutableDuplicator<>());
        registerDuplicator(Boolean.class, new BooleanDuplicator());
        registerDuplicator(boolean.class, new BooleanDuplicator());
//        registerTypeDuplicator(byte[].class, new StandardTypeConverterBytes());
        registerDuplicator(Double.class, new DoubleDuplicator());
        registerDuplicator(double.class, new DoubleDuplicator());
        registerDuplicator(Float.class, new FloatDuplicator());
        registerDuplicator(float.class, new FloatDuplicator());
        registerDuplicator(Integer.class, new IntegerDuplicator());
        registerDuplicator(int.class, new IntegerDuplicator());
        registerDuplicator(Byte.class, new ByteDuplicator());
        registerDuplicator(byte.class, new ByteDuplicator());
        registerDuplicator(Short.class, new ShortDuplicator());
        registerDuplicator(short.class, new ShortDuplicator());
        registerDuplicator(Long.class, new LongDuplicator());
        registerDuplicator(long.class, new LongDuplicator());
        registerDuplicator(java.sql.Date.class, new SqlDateDuplicator());
        registerDuplicator(String.class, new ImmutableDuplicator<>());
        registerDuplicator(Time.class, new SqlTimeDuplicator());
        registerDuplicator(Timestamp.class, new SqlTimestampDuplicator());
        registerDuplicator(java.util.Date.class, new DateDuplicator());
        registerDuplicator(LocalDate.class, new ImmutableDuplicator<>());
        registerDuplicator(LocalDateTime.class, new ImmutableDuplicator<>());
        registerDuplicator(Year.class, new ImmutableDuplicator<>());
        registerDuplicator(Month.class, new ImmutableDuplicator<>());
        registerDuplicator(DayOfWeek.class, new ImmutableDuplicator<>());
        registerDuplicator(UUID.class, new ImmutableDuplicator<>());
        registerDuplicator(Locale.class, new ImmutableDuplicator<>());

    }


    @SuppressWarnings({"java:S1452", "java:S3776"})
    public Optional<Duplicator<?>> getConverter(Field<?, ?, ?> field) {
        Duplicator<?> jc = fieldDuplicators.computeIfAbsent(field, f -> {
            if (f.getFieldClass().isEnum()){
                return new ImmutableDuplicator<>();
            }
            return typeDuplicators.get(f.getFieldClass());
        });
        return Optional.ofNullable(jc);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <E extends Entity> EntityDuplication<E> getEntityDuplication(E entity) {
        return entityDuplications.computeIfAbsent(entity, f -> new EntityDuplication(f, this));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <F> FieldDuplication<F> getFieldDuplication(Field<?, F, ?> entity) {
        return (FieldDuplication<F>) fieldDuplications.computeIfAbsent(entity, f -> new FieldDuplication(f, this));
    }

//    public PlateJsonConversion getPlateJsonConverter(Plate entity) {
//        return plateJsonConversions.computeIfAbsent(entity, f -> new PlateJsonConversion(f, this));
//    }

    public <F> DuplicatorManager registerDuplicator(Class<F> type, Duplicator<F> typeConverter) {
        typeDuplicators.put(type, typeConverter);
        cache.clear();
        return this;
    }

    public void clear() {
        fieldDuplicators.clear();
        typeDuplicators.clear();
        cache.clear();
    }


}
