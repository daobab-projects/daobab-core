package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.time.Period;

/**
 * JSON converter for {@link java.time.Period}: written as its quoted ISO-8601 text
 * (e.g. {@code "P1Y2M3D"}) and parsed with {@link java.time.Period#parse(CharSequence)}.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JsonPeriodConverter extends JsonConverter<Period> {

    @Override
    public void toJson(StringBuilder sb, Period obj) {
        sb.append(QUOTE).append(obj).append(QUOTE);
    }

    @Override
    public Period fromJson(String json) {
        return Period.parse(json);
    }
}
