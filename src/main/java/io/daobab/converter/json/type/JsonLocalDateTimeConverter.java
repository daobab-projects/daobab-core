package io.daobab.converter.json.type;

import io.daobab.converter.JsonConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JsonLocalDateTimeConverter extends JsonConverter<LocalDateTime> {

    static final String dateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    @Override
    public void toJson(StringBuilder sb, LocalDateTime obj) {
        sb.append(QUOTE);
        appendDate(sb, obj.getYear(), obj.getMonthValue(), obj.getDayOfMonth());
        sb.append("T");
        appendTime(sb, obj.getHour(), obj.getMinute(), obj.getSecond(), obj.getNano());
        sb.append(QUOTE);
    }

    @Override
    public LocalDateTime fromJson(String json) {
        return LocalDateTime.parse(json, DateTimeFormatter.ofPattern(dateTimeFormat));
    }
}
