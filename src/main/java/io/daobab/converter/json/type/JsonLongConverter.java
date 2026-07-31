package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

/**
 * JSON converter for {@link Long}: written as a bare JSON number and parsed with
 * {@link Long#parseLong(String)}.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JsonLongConverter extends JsonConverter<Long> {
    @Override
    public void toJson(StringBuilder sb, Long obj) {
        sb.append(obj);
    }

    @Override
    public Long fromJson(String json) {
        return Long.parseLong(json);
    }
}
