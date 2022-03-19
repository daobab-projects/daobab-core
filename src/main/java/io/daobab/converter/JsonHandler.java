package io.daobab.converter;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public interface JsonHandler {

    default String toJSON() {
        return toJSON(this);
    }

    @SuppressWarnings("unchecked")
    default String toJSON(Object val) {
        StringBuilder rv = new StringBuilder();
        if (val == null) {
            rv.append("null");
        } else if (val instanceof Object[]) {
            boolean first = true;
            rv.append("[");
            for (Object o : (Object[]) val) {
                if (!first) rv.append(",");
                first = false;
                rv.append(toJSON(o));
            }
            rv.append("]");
        } else if (val instanceof Collection) {
            boolean first = true;
            rv.append("[");
            for (Object o : (Collection<?>) val) {
                if (o == null) {
                    continue;
                }
                if (!first) rv.append(",");
                first = false;
                rv.append(((JsonHandler) o).toJSON(o));
            }
            rv.append("]");
        } else if (val instanceof Map) {
            rv.append("{");
            boolean first = true;
            Map<Object, Object> map = (Map<Object, Object>) val;

            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                if (entry.getValue() == null) {
                    continue;
                }
                if (!first) rv.append(",");
                first = false;
                rv.append(toJSON(entry.getKey())).append(":");
                rv.append(toJSON(entry.getValue()));
            }
            rv.append("}");

        } else if (val instanceof JsonHandler) {
            rv.append(((JsonHandler) val).toJSON());
        } else if (val instanceof Date) {
            rv.append(JsonDateFormatter.toJSONString((Date) val));
        } else if (val instanceof Number) {
            rv.append(val);
        } else {
            rv.append(JsonEscape.quote(val.toString()));
        }

        return rv.toString();
    }


}
