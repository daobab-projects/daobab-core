package io.daobab.converter.json;

import io.daobab.converter.JsonConverter;

public class JsonByteConverter implements JsonConverter<Byte> {
    @Override
    public String toJson(Byte obj) {
        return String.valueOf(obj);
    }

    @Override
    public Byte fromJson(String json) {
        return Byte.parseByte(json);
    }
}
