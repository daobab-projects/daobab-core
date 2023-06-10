package io.daobab.converter.json.type;

import io.daobab.converter.JsonConverter;

public class JsonIntegerConverter extends JsonConverter<Integer> {
    @Override
    public void toJson(StringBuilder sb, Integer obj) {
        sb.append(obj);
    }

    @Override
    public Integer fromJson(String json) {
        return Integer.parseInt(json);
    }
}
