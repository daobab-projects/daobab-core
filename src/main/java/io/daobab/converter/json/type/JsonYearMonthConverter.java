package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.time.YearMonth;

public class JsonYearMonthConverter extends JsonConverter<YearMonth> {

    @Override
    public void toJson(StringBuilder sb, YearMonth obj) {
        sb.append(QUOTE);
        sb.append(obj.getYear());
        sb.append(PAUSE);
        if (obj.getMonthValue() < 10) {
            sb.append("0");
        }
        sb.append(obj.getMonthValue());
        sb.append(QUOTE);
    }

    @Override
    public YearMonth fromJson(String json) {
        return YearMonth.parse(json);
    }
}
