package io.daobab.creation;

import io.daobab.converter.duplicator.DuplicatorManager;
import io.daobab.error.DaobabEntityCreationException;
import io.daobab.model.Entity;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
