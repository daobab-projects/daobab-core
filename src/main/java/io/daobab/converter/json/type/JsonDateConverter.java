package io.daobab.converter.json.type;

import io.daobab.converter.JsonConverter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class JsonDateConverter extends JsonConverter<Date> {

    private final JsonLocalDateTimeConverter jsonLocalDateTimeConverter = new JsonLocalDateTimeConverter();


    @Override
    public void toJson(StringBuilder sb, Date obj) {
        jsonLocalDateTimeConverter.toJson(sb, LocalDateTime.ofInstant(obj.toInstant(), ZoneId.systemDefault()));
    }

    @Override
    public Date fromJson(String json) {
        LocalDateTime ldt = jsonLocalDateTimeConverter.fromJson(json);
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }
}
