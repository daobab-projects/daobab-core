package io.daobab.clone;

import io.daobab.dict.DictFieldType;
import io.daobab.error.DaobabEntityCreationException;
import io.daobab.error.DaobabException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.Plate;
import io.daobab.model.TableColumn;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public interface EntityDuplicator {

    static <E extends EntityMap> List<E> cloneEntityList(Collection<E> srcCollection) {
        List<E> rv = new ArrayList<>(srcCollection.size());
        int counter = 0;
        for (E src : srcCollection) {
            rv.add(counter, cloneEntity(src));
            counter++;
        }
        return rv;
    }
    static List<Plate> clonePlateList(Collection<Plate> srcCollection) {
        int counter = 0;
        List<Plate> rv = new ArrayList<>(srcCollection.size());
        for (Plate src : srcCollection) {
            rv.add(counter, clonePlate(src));
            counter++;
        }
        return rv;
    }


    static Plate clonePlate(Plate src) {
        if (src.size() == 0) {
            throw new DaobabException("Entity to clone need to have at least one column.");
        }
        Plate clone = null;
        try {
            clone = src.getClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new DaobabEntityCreationException(src.getClass(), e);
        }

        Plate finalClone = clone;
        src.entrySet().forEach(entry->{
            finalClone.put(entry.getKey(),cloneMap(entry.getValue()));
        });

        return clone;
    }

    static Map<String,Object> cloneMap(Map<String,Object> src){
        Map<String,Object> rv=new HashMap<>();
        src.entrySet().forEach(entry->{

            String key=entry.getKey();
            Object val = entry.getValue();
            Class columnClass=val.getClass();
//            if (val == null) continue;
            if (columnClass.equals(DictFieldType.CLASS_BIG_DECIMAL)) {
                rv.put(key, new BigDecimal(val.toString()));
            } else if (columnClass.equals(DictFieldType.CLASS_STRING)) {
                rv.put(key, val.toString());
            } else if (columnClass.equals(DictFieldType.CLASS_DATE_UTIL)) {
                rv.put(key, new Date(((Date) val).getTime()));
            } else if (columnClass.equals(DictFieldType.CLASS_DATE_SQL)) {
                rv.put(key, new java.sql.Date(((java.sql.Date) val).getTime()));
            } else if (columnClass.equals(DictFieldType.CLASS_TIMESTAMP_SQL)) {
                rv.put(key, new Timestamp(((Timestamp) val).getTime()));
            } else if (columnClass.equals(DictFieldType.CLASS_TIME_SQL)) {
                rv.put(key, new Time(((Time) val).getTime()));
            } else if (columnClass.equals(DictFieldType.CLASS_BOOLEAN)) {
                rv.put(key, Boolean.valueOf((boolean) val));
            } else if (columnClass.equals(DictFieldType.CLASS_BIG_INTEGER)) {
                rv.put(key, new BigInteger(val.toString()));
            } else if (columnClass.equals(DictFieldType.CLASS_DOUBLE)) {
                rv.put(key, Double.valueOf(val.toString()));
            } else if (columnClass.equals(DictFieldType.CLASS_FLOAT)) {
                rv.put(key, Float.valueOf(val.toString()));
            } else if (columnClass.equals(DictFieldType.CLASS_LONG)) {
                rv.put(key, Long.valueOf(val.toString()));
            } else if (columnClass.equals(DictFieldType.CLASS_INTEGER)) {
                rv.put(key, Integer.valueOf(val.toString()));
            } else if (columnClass.equals(DictFieldType.CLASS_BYTE_ARRAY)) {
                rv.put(key, ((byte[]) val).clone());
            } else if (columnClass.isInstance(EntityMap.class)) {
                rv.put(key, EntityDuplicator.cloneEntity((EntityMap) val));
            }

//            rv.put(entry.getKey())
        });

        return rv;
    }

    static <E extends EntityMap> E cloneEntity(E src) {
        if (src.columns().size() == 0) {
            throw new DaobabException("Entity to clone need to have at least one column.");
        }
        E clone = null;
        try {
            clone = (E) src.getClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new DaobabEntityCreationException(src.getClass(), e);
        }

        for (TableColumn ecol : src.columns()) {
            Column col = ecol.getColumn();

            Class columnClass = col.getFieldClass();

            Object val = col.getThisValue();
            if (val == null) continue;
            if (columnClass.equals(DictFieldType.CLASS_BIG_DECIMAL)) {
                clone.setColumnParam(col.getFieldName(), new BigDecimal(val.toString()));
            } else if (columnClass.equals(DictFieldType.CLASS_STRING)) {
                clone.setColumnParam(col.getFieldName(), val.toString());
            } else if (columnClass.equals(DictFieldType.CLASS_DATE_UTIL)) {
                clone.setColumnParam(col.getFieldName(), new Date(((Date) val).getTime()));
            } else if (columnClass.equals(DictFieldType.CLASS_DATE_SQL)) {
                clone.setColumnParam(col.getFieldName(), new java.sql.Date(((java.sql.Date) val).getTime()));
            } else if (columnClass.equals(DictFieldType.CLASS_TIMESTAMP_SQL)) {
                clone.setColumnParam(col.getFieldName(), new Timestamp(((Timestamp) val).getTime()));
            } else if (columnClass.equals(DictFieldType.CLASS_TIME_SQL)) {
                clone.setColumnParam(col.getFieldName(), new Time(((Time) val).getTime()));
            } else if (columnClass.equals(DictFieldType.CLASS_BOOLEAN)) {
                clone.setColumnParam(col.getFieldName(), Boolean.valueOf((boolean) val));
            } else if (columnClass.equals(DictFieldType.CLASS_BIG_INTEGER)) {
                clone.setColumnParam(col.getFieldName(), new BigInteger(val.toString()));
            } else if (columnClass.equals(DictFieldType.CLASS_DOUBLE)) {
                clone.setColumnParam(col.getFieldName(), Double.valueOf(val.toString()));
            } else if (columnClass.equals(DictFieldType.CLASS_FLOAT)) {
                clone.setColumnParam(col.getFieldName(), Float.valueOf(val.toString()));
            } else if (columnClass.equals(DictFieldType.CLASS_LONG)) {
                clone.setColumnParam(col.getFieldName(), Long.valueOf(val.toString()));
            } else if (columnClass.equals(DictFieldType.CLASS_INTEGER)) {
                clone.setColumnParam(col.getFieldName(), Integer.valueOf(val.toString()));
            } else if (columnClass.equals(DictFieldType.CLASS_BYTE_ARRAY)) {
                clone.setColumnParam(col.getFieldName(), ((byte[]) val).clone());
            } else if (columnClass.isInstance(EntityMap.class)) {
                clone.setColumnParam(col.getFieldName(), EntityDuplicator.cloneEntity((EntityMap) val));
            }
        }


        return clone;
    }

}
