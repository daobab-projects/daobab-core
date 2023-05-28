package io.daobab.converter.json;

import io.daobab.converter.JsonConverter;

public class JsonBooleanConverter implements JsonConverter<Boolean> {
    @Override
    public String toJson(Boolean obj) {
        return String.valueOf(obj);
    }

    @Override
    public Boolean fromJson(String json) {
        return Boolean.parseBoolean(json);
    }
}
