package io.daobab.converter;

import java.util.Collection;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public interface JsonHandler {

    default String toJSON(Object val) {
        StringBuilder rv = new StringBuilder();
        if (val == null) {
            rv.append("null");
        } else if (val instanceof Object[]) {
            int cnt = 0;
            rv.append("[");
            for (Object o : (Object[]) val) {
                cnt++;
                rv.append(toJSON(o));
                if (cnt < ((Object[]) val).length) {
                    rv.append(",");
                }
            }
            rv.append("]");
        } else if (val instanceof Collection) {
            int cnt = 0;
            int size = ((Collection) val).size();

            rv.append("[");
            for (Object o : (Collection) val) {
                cnt++;
                rv.append(((JsonHandler) o).toJSON(o));
                if (cnt < size) {
                    rv.append(",");
                }
            }
            rv.append("]");
        } else if (val instanceof JsonHandler) {
            rv.append(((JsonHandler) val).toJSON());
        } else if (val instanceof Number) {
            rv.append(val);
        } else {
            rv.append(JsonEscape.quote(val.toString()));
        }

        return rv.toString();
    }

    String toJSON();

}
