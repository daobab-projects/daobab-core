package io.daobab.converter.json.type;

import io.daobab.converter.JsonConverter;

import java.time.DayOfWeek;

public class JsonLocalDayOfWeekConverter extends JsonConverter<DayOfWeek> {


    @Override
    public void toJson(StringBuilder sb, DayOfWeek obj) {
        sb.append(QUOTE).append(obj.getValue());
        sb.append(QUOTE);
    }

    @Override
    public DayOfWeek fromJson(String json) {
         return null;
    }
}
