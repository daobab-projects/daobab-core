package io.daobab.converter.json.type;

import io.daobab.converter.JsonConverter;

import java.time.LocalTime;

public class JsonLocalTimeConverter extends JsonConverter<LocalTime> {


    @Override
    public void toJson(StringBuilder sb, LocalTime obj) {
        sb.append(QUOTE);
        appendTime(sb,obj.getHour(),obj.getMinute(),obj.getSecond(), obj.getNano());
        sb.append(QUOTE);
    }

    @Override
    public LocalTime fromJson(String json) {
         return LocalTime.parse(json);
    }
}
