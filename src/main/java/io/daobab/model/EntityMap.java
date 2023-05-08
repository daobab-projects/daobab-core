package io.daobab.model;

import io.daobab.converter.JsonHandler;
import io.daobab.parser.ParserNumber;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface EntityMap extends Entity, Map<String, Object>, MapParameterHandler, JsonHandler {

    @SuppressWarnings("unchecked")
    @Override
    default <X> X getColumnParam(String key) {
        return (X) get(key);
    }

    @Override
    default <X> void setColumnParam(String key, X param) {
        put(key, param);
    }


    default void fixSerialisation() {
        for (TableColumn tableColumn : columns()) {
            Column<?, ?, ?> column = tableColumn.getColumn();
            Object value = getColumnParam(column.getFieldName());
            if (value == null) continue;

            if (column.getFieldClass().equals(BigDecimal.class)) {
                if (value instanceof BigDecimal) continue;
                setColumnParam(column.getFieldName(), ParserNumber.toBigDecimal((Number) value));
            } else if (column.getFieldClass().equals(Timestamp.class)) {
                if (value instanceof Timestamp) continue;
                setColumnParam(column.getFieldName(), new Timestamp((long) value));
            } else if (value instanceof EntityMap) {
                EntityMap em = (EntityMap) value;
                em.fixSerialisation();
            }
        }
    }


}
