package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.time.LocalTime;

/**
 * JSON converter for {@link java.time.LocalTime}: written as a quoted {@code HH:mm:ss.SSS} value
 * (millisecond precision) and parsed with {@link java.time.LocalTime#parse(CharSequence)}.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JsonLocalTimeConverter extends JsonConverter<LocalTime> {

    @Override
    public void toJson(StringBuilder sb, LocalTime obj) {
        sb.append(QUOTE);
        appendTime(sb, obj.getHour(), obj.getMinute(), obj.getSecond(), obj.getNano());
        sb.append(QUOTE);
    }

    @Override
    public LocalTime fromJson(String json) {
        return LocalTime.parse(json);
    }
}
