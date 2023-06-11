package io.daobab.converter.json.type;

import io.daobab.converter.JsonConverter;

import java.sql.Timestamp;

public class JsonTimestampConverter extends JsonConverter<Timestamp> {

    private final JsonLocalDateTimeConverter jsonLocalDateTimeConverter = new JsonLocalDateTimeConverter();

    @Override
    public void toJson(StringBuilder sb, Timestamp obj) {
        jsonLocalDateTimeConverter.toJson(sb, obj.toLocalDateTime());
    }

    @Override
    public Timestamp fromJson(String json) {
        return Timestamp.valueOf(jsonLocalDateTimeConverter.fromJson(json));

    }
}
