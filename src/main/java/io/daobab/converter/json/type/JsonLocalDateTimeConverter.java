package io.daobab.converter.json.type;

import io.daobab.converter.JsonConverter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

public class JsonLocalDateTimeConverter extends JsonConverter<LocalDateTime> {

    @Override
    public void toJson(StringBuilder sb, LocalDateTime obj) {
        sb.append(QUOTE);
        appendDate(sb, obj.getYear(), obj.getMonthValue(), obj.getDayOfMonth());
        appendTime(sb,obj.getHour(),obj.getMinute(),obj.getSecond(), obj.getNano());
        sb.append(QUOTE);
    }

    @Override
    public LocalDateTime fromJson(String json) {
         return LocalDateTime.parse(json);
    }
}
