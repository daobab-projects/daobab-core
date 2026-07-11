package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.time.Period;

public class JsonPeriodConverter extends JsonConverter<Period> {

    @Override
    public void toJson(StringBuilder sb, Period obj) {
        sb.append(QUOTE).append(obj).append(QUOTE);
    }

    @Override
    public Period fromJson(String json) {
        return Period.parse(json);
    }
}
