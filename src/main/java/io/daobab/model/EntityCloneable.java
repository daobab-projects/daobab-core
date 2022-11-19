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
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public interface EntityCloneable<E extends Entity> extends Entity {

    @SuppressWarnings({"java:S3740", "unchecked", "rawtypes"})
    default E clone() {
        if (columns().isEmpty()) {
            throw new DaobabException("Entity to clone need to have at least one column.");
        }
        E clone = null;
        try {
            clone = (E) this.columns().get(0).getClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        for (TableColumn tableColumn : this.columns()) {
            Column column = tableColumn.getColumn();
            Class<?> columnClass = column.getFieldClass();
            Object value = column.getThisValue();
            if (value == null) continue;
            if (columnClass.equals(DictFieldType.CLASS_BIG_DECIMAL)) {
                column.setValue((EntityRelation) clone, new BigDecimal(value.toString()));
            } else if (columnClass.equals(DictFieldType.CLASS_STRING)) {
                column.setValue((EntityRelation) clone, value.toString());
            } else if (columnClass.equals(DictFieldType.CLASS_DATE_UTIL)) {
                column.setValue((EntityRelation) clone, new Date(((Date) value).getTime()));
            } else if (columnClass.equals(DictFieldType.CLASS_DATE_SQL)) {
                column.setValue((EntityRelation) clone, new java.sql.Date(((java.sql.Date) value).getTime()));
            } else if (columnClass.equals(DictFieldType.CLASS_TIMESTAMP_SQL)) {
                column.setValue((EntityRelation) clone, new Timestamp(((Timestamp) value).getTime()));
            } else if (columnClass.equals(DictFieldType.CLASS_TIME_SQL)) {
                column.setValue((EntityRelation) clone, new Time(((Time) value).getTime()));
            } else if (columnClass.equals(DictFieldType.CLASS_BOOLEAN)) {
                column.setValue((EntityRelation) clone, value);
            } else if (columnClass.equals(DictFieldType.CLASS_BIG_INTEGER)) {
                column.setValue((EntityRelation) clone, new BigInteger(value.toString()));
            } else if (columnClass.equals(DictFieldType.CLASS_DOUBLE)) {
                column.setValue((EntityRelation) clone, Double.parseDouble(value.toString()));
            } else if (columnClass.equals(DictFieldType.CLASS_FLOAT)) {
                column.setValue((EntityRelation) clone, Float.parseFloat(value.toString()));
            } else if (columnClass.equals(DictFieldType.CLASS_LONG)) {
                column.setValue((EntityRelation) clone, Long.parseLong(value.toString()));
            } else if (columnClass.equals(DictFieldType.CLASS_INTEGER)) {
                column.setValue((EntityRelation) clone, Integer.parseInt(value.toString()));
            } else if (columnClass.equals(DictFieldType.CLASS_BYTE_ARRAY)) {
                column.setValue((EntityRelation) clone, ((byte[]) value).clone());
            }
        }


        return clone;
    }

}
