package io.daobab.converter.json.type;

import io.daobab.converter.JsonConverter;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class JsonTimestampConverter extends JsonConverter<Timestamp> {

    private final JsonLocalDateTimeConverter jsonLocalDateTimeConverter=new JsonLocalDateTimeConverter();

    @Override
    public void toJson(StringBuilder sb, Timestamp obj) {
        jsonLocalDateTimeConverter.toJson(sb,obj.toLocalDateTime());
    }

    @Override
    public Timestamp fromJson(String json) {
        return null;

    }
}
