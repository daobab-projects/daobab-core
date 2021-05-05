package io.daobab.model;

import io.daobab.converter.JsonMarshaller;
import io.daobab.parser.ParserNumber;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public interface EntityMap extends Entity, Map<String, Object>, MapParameterHandler, JsonMarshaller {

    @Override
    default <X> X getColumnParam(String key) {
        return (X) get(key);
    }

    @Override
    default <X> void setColumnParam(String key, X param) {
        put(key, param);
    }


    default void fixSerialisation() {
        for (TableColumn ecol : columns()) {
            Column col = ecol.getColumn();
            Object val = getColumnParam(col.getFieldName());
            if (val == null) continue;

            if (col.getFieldClass().equals(BigDecimal.class)) {
                if (val instanceof BigDecimal) continue;
                setColumnParam(col.getFieldName(), ParserNumber.toBigDecimal((Number) val));
            } else if (col.getFieldClass().equals(Timestamp.class)) {
                if (val instanceof Timestamp) continue;
                setColumnParam(col.getFieldName(), new Timestamp((long) val));
            } else if (val instanceof EntityMap) {
                EntityMap em = (EntityMap) val;
                em.fixSerialisation();
            }
        }
    }


}
