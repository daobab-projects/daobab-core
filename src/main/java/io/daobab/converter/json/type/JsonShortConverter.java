package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

public class JsonShortConverter extends JsonConverter<Short> {
    @Override
    public void toJson(StringBuilder sb, Short obj) {
        sb.append(obj);
    }

    @Override
    public Short fromJson(String json) {
        return Short.parseShort(json);
    }
}
