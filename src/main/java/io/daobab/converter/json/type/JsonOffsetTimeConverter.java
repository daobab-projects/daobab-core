package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.time.OffsetTime;

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
