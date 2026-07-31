package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.util.Optional;

/**
 * JSON converter for an {@link java.util.Optional}: an empty optional is written as {@code null},
 * otherwise the wrapped value is serialized by the {@code innerTypeConverter}; {@code null}/empty text reads
 * back to {@link java.util.Optional#empty()}.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JsonOptionalConverter extends JsonConverter<Optional> {

    final JsonConverter innerTypeConverter;

    public JsonOptionalConverter(JsonConverter innerTypeConverter) {
        this.innerTypeConverter = innerTypeConverter;
    }

    @Override
    public void toJson(StringBuilder sb, Optional obj) {
        if (obj.isPresent()) {
            innerTypeConverter.toJson(sb, obj.get());
        } else {
            sb.append("null");
        }
    }

    @Override
    public Optional fromJson(String json) {
        if (json == null || "null".equals(json.trim()) || json.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(innerTypeConverter.fromJson(json));
        }
    }

}
