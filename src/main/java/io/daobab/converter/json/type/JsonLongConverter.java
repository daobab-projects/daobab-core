package io.daobab.converter.json.type;

import io.daobab.converter.JsonConverter;

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
