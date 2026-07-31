package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.time.MonthDay;

/**
 * JSON converter for {@link java.time.MonthDay}: written as its quoted ISO text
 * (e.g. {@code "--07-11"}) and parsed with {@link java.time.MonthDay#parse(CharSequence)}.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JsonMonthDayConverter extends JsonConverter<MonthDay> {

    @Override
    public void toJson(StringBuilder sb, MonthDay obj) {
        sb.append(QUOTE).append(obj).append(QUOTE);
    }

    @Override
    public MonthDay fromJson(String json) {
        return MonthDay.parse(json);
    }
}
