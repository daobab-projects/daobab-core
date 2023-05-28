package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

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
