package io.daobab.converter.json.type;

import io.daobab.converter.JsonConverter;

public class JsonBooleanConverter extends JsonConverter<Boolean> {
    @Override
    public void toJson(StringBuilder sb, Boolean obj) {
        sb.append(obj);
    }

    @Override
    public Boolean fromJson(String json) {
        return Boolean.parseBoolean(json);
    }
}
