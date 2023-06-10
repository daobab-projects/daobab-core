package io.daobab.converter.json.type;

import io.daobab.converter.JsonConverter;

import java.time.Month;

public class JsonLocalMonthConverter extends JsonConverter<Month> {


    @Override
    public void toJson(StringBuilder sb, Month obj) {
        sb.append(QUOTE).append(obj.getValue());
        sb.append(QUOTE);
    }

    @Override
    public Month fromJson(String json) {
         return null;
    }
}
