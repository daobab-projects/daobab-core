package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

/**
 * JSON converter for {@link Boolean}: written as the bare literal {@code true}/{@code false} and
 * parsed with {@link Boolean#parseBoolean(String)}.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JsonBooleanConverter extends JsonConverter<Boolean> {
    @Override
    public void toJson(StringBuilder sb, Boolean obj) {
        sb.append(obj);
    }

    @Override
    public Boolean fromJson(String json) {
        return Boolean.parseBoolean(json);
    }
}
