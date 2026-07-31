package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * JSON converter for {@link java.time.Instant}: written as the UTC {@link java.time.LocalDateTime}
 * ({@code yyyy-MM-dd'T'HH:mm:ss.SSS}) and read back as the instant at that UTC time.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JsonInstantConverter extends JsonConverter<Instant> {

    private final JsonLocalDateTimeConverter jsonLocalDateConverter = new JsonLocalDateTimeConverter();

    @Override
    public void toJson(StringBuilder sb, Instant obj) {
        LocalDateTime ldt = LocalDateTime.ofInstant(obj, ZoneOffset.UTC);
        jsonLocalDateConverter.toJson(sb, ldt);
    }

    @Override
    public Instant fromJson(String json) {
        return jsonLocalDateConverter.fromJson(json).toInstant(ZoneOffset.UTC);
    }
}
