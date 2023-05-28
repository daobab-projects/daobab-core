package io.daobab.converter.json;

import io.daobab.converter.JsonConverter;

public class JsonShortConverter implements JsonConverter<Short> {
    @Override
    public String toJson(Short obj) {
        return String.valueOf(obj);
    }

    @Override
    public Short fromJson(String json) {
        return Short.parseShort(json);
    }
}
