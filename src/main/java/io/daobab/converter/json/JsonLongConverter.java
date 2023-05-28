package io.daobab.converter.json;

import io.daobab.converter.JsonConverter;

public class JsonLongConverter implements JsonConverter<Long> {
    @Override
    public String toJson(Long obj) {
        return String.valueOf(obj);
    }

    @Override
    public Long fromJson(String json) {
        return Long.parseLong(json);
    }
}
