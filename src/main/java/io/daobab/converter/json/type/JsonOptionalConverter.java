package io.daobab.converter.json.type;

import io.daobab.converter.JsonConverter;

import java.util.Optional;

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
