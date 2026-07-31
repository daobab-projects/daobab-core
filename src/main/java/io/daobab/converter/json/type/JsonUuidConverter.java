package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.util.UUID;

/**
 * JSON converter for {@link java.util.UUID}: written as its quoted canonical text and parsed with
 * {@link java.util.UUID#fromString(String)}.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JsonUuidConverter extends JsonConverter<UUID> {

    @Override
    public void toJson(StringBuilder sb, UUID obj) {
        sb.append(QUOTE).append(obj).append(QUOTE);
    }

    @Override
    public UUID fromJson(String json) {
        return UUID.fromString(json);
    }
}
