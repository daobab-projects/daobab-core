package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.time.LocalDate;

public class JsonLocalDateConverter extends JsonConverter<LocalDate> {


    @Override
    public void toJson(StringBuilder sb, LocalDate obj) {
        sb.append(QUOTE);
        appendDate(sb, obj.getYear(), obj.getMonthValue(), obj.getDayOfMonth());
        sb.append(QUOTE);
    }

    @Override
    public LocalDate fromJson(String json) {
        return LocalDate.parse(json);
    }
}
