package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.time.LocalDate;

/**
 * JSON converter for {@link java.time.LocalDate}: written as a quoted {@code yyyy-MM-dd} value and
 * parsed with {@link java.time.LocalDate#parse(CharSequence)}.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JsonLocalDateConverter extends JsonConverter<LocalDate> {


    @Override
    public void toJson(StringBuilder sb, LocalDate obj) {
        sb.append(QUOTE);
        appendDate(sb, obj.getYear(), obj.getMonthValue(), obj.getDayOfMonth());
        sb.append(QUOTE);
    }

    @Override
    public LocalDate fromJson(String json) {
        return LocalDate.parse(json);
    }
}
