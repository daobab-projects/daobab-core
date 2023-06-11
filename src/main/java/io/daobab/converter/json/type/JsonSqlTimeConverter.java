package io.daobab.converter.json.type;

import io.daobab.converter.JsonConverter;

import java.sql.Time;

public class JsonSqlTimeConverter extends JsonConverter<Time> {

    private final JsonLocalTimeConverter jsonLocalTimeConverter = new JsonLocalTimeConverter();


    @Override
    public void toJson(StringBuilder sb, Time obj) {
        jsonLocalTimeConverter.toJson(sb, obj.toLocalTime());
    }

    @Override
    public Time fromJson(String json) {
        return Time.valueOf(jsonLocalTimeConverter.fromJson(json));
    }
}
