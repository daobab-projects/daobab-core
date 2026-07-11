package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.time.OffsetDateTime;

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
