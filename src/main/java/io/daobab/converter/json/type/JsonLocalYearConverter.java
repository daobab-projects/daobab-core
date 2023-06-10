package io.daobab.converter.json.type;

import io.daobab.converter.JsonConverter;

import java.time.Year;

public class JsonLocalYearConverter extends JsonConverter<Year> {


    @Override
    public void toJson(StringBuilder sb, Year obj) {
        sb.append(QUOTE).append(obj.getValue());
        sb.append(QUOTE);
    }

    @Override
    public Year fromJson(String json) {
         return null;
    }
}
