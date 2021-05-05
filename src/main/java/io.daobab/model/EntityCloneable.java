package io.daobab.model;

import io.daobab.dict.DictFieldType;
import io.daobab.error.DaobabException;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public interface EntityCloneable<E extends Entity> extends Entity {


    @SuppressWarnings("java:S3740")
    default E clone() {
        if (columns().isEmpty()) {
            throw new DaobabException("Entity to clone need to have at least one column.");
        }
        E clone = null;
        try {
            clone = (E) this.columns().get(0).getClass().getDeclaredConstructor().newInstance();
            ;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        for (TableColumn ecol : this.columns()) {

            Column col = ecol.getColumn();

            Class<?> columnClass = col.getFieldClass();

            Object val = col.getThisValue();
            if (columnClass.equals(DictFieldType.CLASS_BIG_DECIMAL)) {
                col.setValue((EntityRelation) clone, val == null ? null : new BigDecimal(val.toString()));
            } else if (columnClass.equals(DictFieldType.CLASS_STRING)) {
                col.setValue((EntityRelation) clone, val == null ? null : val.toString());
            } else if (columnClass.equals(DictFieldType.CLASS_DATE_UTIL)) {
                col.setValue((EntityRelation) clone, val == null ? null : new Date(((Date) val).getTime()));
            } else if (columnClass.equals(DictFieldType.CLASS_DATE_SQL)) {
                col.setValue((EntityRelation) clone, val == null ? null : new java.sql.Date(((java.sql.Date) val).getTime()));
            } else if (columnClass.equals(DictFieldType.CLASS_TIMESTAMP_SQL)) {
                col.setValue((EntityRelation) clone, val == null ? null : new Timestamp(((Timestamp) val).getTime()));
            } else if (columnClass.equals(DictFieldType.CLASS_TIME_SQL)) {
                col.setValue((EntityRelation) clone, val == null ? null : new Time(((Time) val).getTime()));
            } else if (columnClass.equals(DictFieldType.CLASS_BOOLEAN)) {
                col.setValue((EntityRelation) clone, val == null ? null : (boolean) val == true);
            } else if (columnClass.equals(DictFieldType.CLASS_BIG_INTEGER)) {
                col.setValue((EntityRelation) clone, val == null ? null : new BigInteger(val.toString()));
            } else if (columnClass.equals(DictFieldType.CLASS_DOUBLE)) {
                col.setValue((EntityRelation) clone, val == null ? null : Double.parseDouble(val.toString()));
            } else if (columnClass.equals(DictFieldType.CLASS_FLOAT)) {
                col.setValue((EntityRelation) clone, val == null ? null : Float.parseFloat(val.toString()));
            } else if (columnClass.equals(DictFieldType.CLASS_LONG)) {
                col.setValue((EntityRelation) clone, val == null ? null : Long.parseLong(val.toString()));
            } else if (columnClass.equals(DictFieldType.CLASS_INTEGER)) {
                col.setValue((EntityRelation) clone, val == null ? null : Integer.parseInt(val.toString()));
            } else if (columnClass.equals(DictFieldType.CLASS_BYTE_ARRAY)) {
                col.setValue((EntityRelation) clone, val == null ? null : ((byte[]) val).clone());
            }
        }


        return clone;
    }

}
