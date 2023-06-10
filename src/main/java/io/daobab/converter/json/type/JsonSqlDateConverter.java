package io.daobab.converter.json.type;

import io.daobab.converter.JsonConverter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.sql.Date;

public class JsonSqlDateConverter extends JsonConverter<Date> {

    private final JsonLocalDateTimeConverter jsonLocalDateTimeConverter=new JsonLocalDateTimeConverter();

    @Override
    public void toJson(StringBuilder sb, Date obj) {
        jsonLocalDateTimeConverter.toJson(sb, LocalDateTime.ofInstant(obj.toInstant(), ZoneId.systemDefault()));
    }

    @Override
    public Date fromJson(String json) {
        return null;
    }
}
