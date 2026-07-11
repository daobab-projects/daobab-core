package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.time.MonthDay;

public class JsonMonthDayConverter extends JsonConverter<MonthDay> {

    @Override
    public void toJson(StringBuilder sb, MonthDay obj) {
        sb.append(QUOTE).append(obj).append(QUOTE);
    }

    @Override
    public MonthDay fromJson(String json) {
        return MonthDay.parse(json);
    }
}
