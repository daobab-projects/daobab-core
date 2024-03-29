package io.daobab.creation;

import io.daobab.converter.duplicator.DuplicatorManager;
import io.daobab.converter.json.JsonConverterManager;
import io.daobab.converter.json.conversion.EntityJsonConversion;
import io.daobab.error.DaobabEntityCreationException;
import io.daobab.error.DaobabException;
import io.daobab.model.Entity;
import io.daobab.target.buffer.single.Entities;
import io.daobab.target.buffer.single.EntityList;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public final class EntityCreator {

    private EntityCreator() {
    }

    public static <E extends Entity> EntityBuilder<E> builder(Class<E> entityClass) {
        return new EntityBuilder<>(entityClass);
    }

    public static <E extends Entity> E createEntity(Class<E> entityClass) {
        return createEntity(entityClass, new HashMap<>());
    }

    public static <E extends Entity> E createEntityFromJson(Class<E> entityClass, String json) {
        EntityJsonConversion<E> entityJsonConverter = JsonConverterManager.INSTANCE.getEntityJsonConverter(createEntity(entityClass));
        return entityJsonConverter.fromJson(entityClass, json);
    }

    public static <E extends Entity> Entities<E> createEntityListFromJson(Class<E> entityClass, String json) {
        String js = json.trim();
        if (!js.startsWith("[") || !js.endsWith("]")) {
            throw new DaobabException("Cannot convert an json array");
        }
        js = js.substring(js.indexOf("["), js.lastIndexOf("]"));
        Matcher matcher = Pattern.compile("\\{[^}]*\\}").matcher(js);

        List<String> arrays = new ArrayList<>();
        while (matcher.find()) {
            arrays.add(matcher.group());
        }

        EntityJsonConversion<E> entityJsonConverter = JsonConverterManager.INSTANCE.getEntityJsonConverter(createEntity(entityClass));

        List<E> rv = arrays.stream().map(j -> entityJsonConverter.fromJson(entityClass, j)).collect(Collectors.toList());

        return new EntityList<>(rv, entityClass);
    }

    public static <E extends Entity> Entities<E> createEntities(List<Map<String, Object>> list, Class<E> entityClass) {
        return new EntityList<>(list.stream().map(map -> createEntity(entityClass, map)).collect(Collectors.toList()), entityClass);
    }

    public static <E extends Entity> E createEntity(Class<E> entityClass, Map<String, Object> map) {
        try {
            return entityClass.getDeclaredConstructor(Map.class).newInstance(Collections.unmodifiableMap(new LinkedHashMap<>(map)));
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new DaobabEntityCreationException(entityClass, e);
        }
    }
//
//    public static List<Plate> clonePlateList(Collection<Plate> srcCollection) {
//        int counter = 0;
//        List<Plate> rv = new ArrayList<>(srcCollection.size());
//        for (Plate src : srcCollection) {
//            rv.add(counter, clonePlate(src));
//            counter++;
//        }
//        return rv;
//    }
//
//
//    static Plate clonePlate(Plate src) {
//        if (src.size() == 0) {
//            throw new DaobabException("Entity to clone need to have at least one column.");
//        }
//        Plate clone;
//        try {
//            clone = src.getClass().getDeclaredConstructor().newInstance();
//        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
//            throw new DaobabEntityCreationException(src.getClass(), e);
//        }
//
//        Plate finalClone = clone;
//        src.forEach((key, value) -> finalClone.put(key, cloneMap(value)));
//
//        return clone;
//    }
//
//    public static Map<String, Object> cloneMap(Map<String, Object> src) {
//        Map<String, Object> rv = new HashMap<>();
//        src.forEach((key, val) -> {
//
//
//            Class columnClass = val.getClass();
////            if (val == null) continue;
//            if (columnClass.equals(DictFieldType.CLASS_BIG_DECIMAL)) {
//                rv.put(key, new BigDecimal(val.toString()));
//            } else if (columnClass.equals(DictFieldType.CLASS_STRING)) {
//                rv.put(key, val.toString());
//            } else if (columnClass.equals(DictFieldType.CLASS_DATE_UTIL)) {
//                rv.put(key, new Date(((Date) val).getTime()));
//            } else if (columnClass.equals(DictFieldType.CLASS_DATE_SQL)) {
//                rv.put(key, new java.sql.Date(((java.sql.Date) val).getTime()));
//            } else if (columnClass.equals(DictFieldType.CLASS_TIMESTAMP_SQL)) {
//                rv.put(key, new Timestamp(((Timestamp) val).getTime()));
//            } else if (columnClass.equals(DictFieldType.CLASS_TIME_SQL)) {
//                rv.put(key, new Time(((Time) val).getTime()));
//            } else if (columnClass.equals(DictFieldType.CLASS_BOOLEAN)) {
//                rv.put(key, val);
//            } else if (columnClass.equals(DictFieldType.CLASS_BIG_INTEGER)) {
//                rv.put(key, new BigInteger(val.toString()));
//            } else if (columnClass.equals(DictFieldType.CLASS_DOUBLE)) {
//                rv.put(key, Double.valueOf(val.toString()));
//            } else if (columnClass.equals(DictFieldType.CLASS_FLOAT)) {
//                rv.put(key, Float.valueOf(val.toString()));
//            } else if (columnClass.equals(DictFieldType.CLASS_LONG)) {
//                rv.put(key, Long.valueOf(val.toString()));
//            } else if (columnClass.equals(DictFieldType.CLASS_INTEGER)) {
//                rv.put(key, Integer.valueOf(val.toString()));
//            } else if (columnClass.equals(DictFieldType.CLASS_BYTE_ARRAY)) {
//                rv.put(key, ((byte[]) val).clone());
//            }
//
////            rv.put(entry.getKey())
//        });
//
//        return rv;
//    }

    public static <E extends Entity> E cloneEntity(E src) {
        return DuplicatorManager.INSTANCE.getEntityDuplication(src).duplicate(src);
    }

}
