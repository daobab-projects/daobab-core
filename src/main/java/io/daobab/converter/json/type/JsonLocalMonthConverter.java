package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.time.Month;

/**
 * JSON converter for {@link java.time.Month}: written as its quoted numeric value 1-12 and parsed
 * with {@link java.time.Month#of(int)} (numeric, not the enum name).
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JsonLocalMonthConverter extends JsonConverter<Month> {


    @Override
    public void toJson(StringBuilder sb, Month obj) {
        sb.append(QUOTE).append(obj.getValue())
                .append(QUOTE);
    }

    @Override
    public Month fromJson(String json) {
        return Month.of(Integer.parseInt(json));
    }
}
