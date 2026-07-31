package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.time.OffsetDateTime;

/**
 * JSON converter for {@link java.time.OffsetDateTime}: written as a quoted date-time with its offset
 * appended and parsed with {@link java.time.OffsetDateTime#parse(CharSequence)}.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JsonOffsetDateTimeConverter extends JsonConverter<OffsetDateTime> {

    @Override
    public void toJson(StringBuilder sb, OffsetDateTime obj) {
        sb.append(QUOTE);
        appendDate(sb, obj.getYear(), obj.getMonthValue(), obj.getDayOfMonth());
        sb.append("T");
        appendTime(sb, obj.getHour(), obj.getMinute(), obj.getSecond(), obj.getNano());
        appendTimeZone(sb, obj.getOffset());
        sb.append(QUOTE);
    }

    @Override
    public OffsetDateTime fromJson(String json) {
        return OffsetDateTime.parse(json);
    }
}
