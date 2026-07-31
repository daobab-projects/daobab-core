package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.sql.Time;

/**
 * JSON converter for {@link java.sql.Time}: delegates to {@link JsonLocalTimeConverter} through the
 * time's {@code toLocalTime()} form (quoted {@code HH:mm:ss.SSS}).
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
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
