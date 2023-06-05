package io.daobab.converter.json.type;

import io.daobab.converter.JsonConverter;

import java.time.LocalTime;

public class JsonLocalTimeConverter implements JsonConverter<LocalTime> {


    @Override
    public String toJson(LocalTime obj) {
        return obj.toString();
    }

    @Override
    public LocalTime fromJson(String json) {
         return LocalTime.parse(json);
    }
}
