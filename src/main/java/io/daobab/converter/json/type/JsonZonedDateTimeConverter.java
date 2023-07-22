package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.time.ZonedDateTime;

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
