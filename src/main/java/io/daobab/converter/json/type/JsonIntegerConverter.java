package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

/**
 * JSON converter for {@link Integer}: written as a bare JSON number and parsed with
 * {@link Integer#parseInt(String)}.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JsonIntegerConverter extends JsonConverter<Integer> {
    @Override
    public void toJson(StringBuilder sb, Integer obj) {
        sb.append(obj);
    }

    @Override
    public Integer fromJson(String json) {
        return Integer.parseInt(json);
    }
}
