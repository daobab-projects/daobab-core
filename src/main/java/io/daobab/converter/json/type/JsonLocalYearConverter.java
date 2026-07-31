package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.time.Year;

/**
 * JSON converter for {@link java.time.Year}: written as its quoted numeric value
 * (e.g. {@code "2026"}) and parsed with {@link java.time.Year#of(int)}.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JsonLocalYearConverter extends JsonConverter<Year> {


    @Override
    public void toJson(StringBuilder sb, Year obj) {
        sb.append(QUOTE).append(obj.getValue());
        sb.append(QUOTE);
    }

    @Override
    public Year fromJson(String json) {
        return Year.of(Integer.parseInt(json));
    }
}
