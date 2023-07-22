package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.sql.Date;

public class JsonSqlDateConverter extends JsonConverter<Date> {

    private final JsonLocalDateConverter jsonLocalDateTimeConverter = new JsonLocalDateConverter();

    @Override
    public void toJson(StringBuilder sb, Date obj) {
        jsonLocalDateTimeConverter.toJson(sb, obj.toLocalDate());
    }

    @Override
    public Date fromJson(String json) {
        return Date.valueOf(jsonLocalDateTimeConverter.fromJson(json));
    }
}
