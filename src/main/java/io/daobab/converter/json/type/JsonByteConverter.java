package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

/**
 * JSON converter for {@link Byte}: written as its decimal text (a bare JSON number) and parsed
 * with {@link Byte#parseByte(String)}.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
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
