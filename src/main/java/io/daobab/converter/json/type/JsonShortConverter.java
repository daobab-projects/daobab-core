package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

/**
 * JSON converter for {@link Short}: written as a bare JSON number and parsed with
 * {@link Short#parseShort(String)}.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
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
