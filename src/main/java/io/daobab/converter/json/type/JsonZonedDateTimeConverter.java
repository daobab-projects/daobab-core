package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.time.ZonedDateTime;

/**
 * JSON converter for {@link java.time.ZonedDateTime}: written as a quoted date-time with the zone
 * offset appended (e.g. {@code "2026-07-11T13:05:09.123+02:00"}) and parsed with
 * {@link java.time.ZonedDateTime#parse(CharSequence)}.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JsonZonedDateTimeConverter extends JsonConverter<ZonedDateTime> {

    @Override
    public void toJson(StringBuilder sb, ZonedDateTime obj) {
        sb.append(QUOTE);
        appendDate(sb, obj.getYear(), obj.getMonthValue(), obj.getDayOfMonth());
        sb.append("T");
        appendTime(sb, obj.getHour(), obj.getMinute(), obj.getSecond(), obj.getNano());
        appendTimeZone(sb, obj.getOffset());
        sb.append(QUOTE);
    }

    @Override
    public ZonedDateTime fromJson(String json) {
        return ZonedDateTime.parse(json);
    }
}
