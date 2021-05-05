package io.daobab.converter;

import java.util.Map;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public interface JsonMarshaller extends JsonHandler, Map<String, Object> {


    default String toJSON() {
        StringBuilder rv = new StringBuilder();
        rv.append("{");

        int size = keySet().size();
        int cnt = 0;

        for (Entry<String,Object> entry : entrySet()) {
            cnt++;
            boolean lastOne = cnt == size;
            rv.append("\"").append(entry.getKey()).append("\":");
            rv.append(toJSON(entry.getValue()));
            if (!lastOne) rv.append(",");
        }

        rv.append("}");

        return rv.toString();

    }

}
