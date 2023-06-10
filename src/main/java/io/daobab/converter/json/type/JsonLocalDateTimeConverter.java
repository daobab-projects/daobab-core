package io.daobab.converter.json.type;

import io.daobab.converter.JsonConverter;

import java.time.LocalDateTime;

public class JsonLocalDateTimeConverter implements JsonConverter<LocalDateTime> {

//    final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddThh:mm:ss");

    @Override
    public String toJson(LocalDateTime obj) {
        return "\""+obj.toString()+"\"";
    }

    @Override
    public LocalDateTime fromJson(String json) {
         return LocalDateTime.parse(json);
    }
}
