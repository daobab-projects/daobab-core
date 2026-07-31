package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.time.YearMonth;

/**
 * JSON converter for {@link java.time.YearMonth}: written as a quoted {@code yyyy-MM} value with the
 * month zero-padded, and parsed with {@link java.time.YearMonth#parse(CharSequence)}.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JsonYearMonthConverter extends JsonConverter<YearMonth> {

    @Override
    public void toJson(StringBuilder sb, YearMonth obj) {
        sb.append(QUOTE);
        appendPaddedYear(sb, obj.getYear());
        sb.append(PAUSE);
        if (obj.getMonthValue() < 10) {
            sb.append("0");
        }
        sb.append(obj.getMonthValue());
        sb.append(QUOTE);
    }

    @Override
    public YearMonth fromJson(String json) {
        return YearMonth.parse(json);
    }
}
