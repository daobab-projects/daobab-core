package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.time.ZoneId;

/**
 * JSON converter for {@link java.time.ZoneId}: written as its quoted zone id text (e.g. {@code "Europe/Warsaw"})
 * and parsed with {@link java.time.ZoneId#of(String)}. Complements the date-time converters when a column stores
 * a zone on its own.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JsonZoneIdConverter extends JsonConverter<ZoneId> {

    @Override
    public void toJson(StringBuilder sb, ZoneId obj) {
        sb.append(QUOTE).append(obj.getId()).append(QUOTE);
    }

    @Override
    public ZoneId fromJson(String json) {
        return ZoneId.of(json);
    }
}
