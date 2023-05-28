package io.daobab.converter.json;

import io.daobab.converter.JsonConverter;

public class JsonIntegerConverter implements JsonConverter<Integer> {
    @Override
    public String toJson(Integer obj) {
        return String.valueOf(obj);
    }

    @Override
    public Integer fromJson(String json) {
        return Integer.parseInt(json);
    }
}
