package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.time.OffsetTime;

/**
 * JSON converter for {@link java.time.OffsetTime}: written as a quoted time with its offset appended
 * (e.g. {@code "08:30:15.007-05:00"}) and parsed with {@link java.time.OffsetTime#parse(CharSequence)}.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JsonOffsetTimeConverter extends JsonConverter<OffsetTime> {

    @Override
    public void toJson(StringBuilder sb, OffsetTime obj) {
        sb.append(QUOTE);
        appendTime(sb, obj.getHour(), obj.getMinute(), obj.getSecond(), obj.getNano());
        appendTimeZone(sb, obj.getOffset());
        sb.append(QUOTE);
    }

    @Override
    public OffsetTime fromJson(String json) {
        return OffsetTime.parse(json);
    }
}
