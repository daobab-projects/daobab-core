package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.sql.Timestamp;

/**
 * JSON converter for {@link java.sql.Timestamp}: delegates to {@link JsonLocalDateTimeConverter}
 * through the timestamp's {@code toLocalDateTime()} form (millisecond precision).
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
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
