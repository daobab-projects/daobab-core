package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

public class JsonByteConverter extends JsonConverter<Byte> {
    @Override
    public void toJson(StringBuilder sb, Byte obj) {
        sb.append(obj);
    }

    @Override
    public Byte fromJson(String json) {
        return Byte.parseByte(json);
    }
}
