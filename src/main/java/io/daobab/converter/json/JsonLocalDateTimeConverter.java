package io.daobab.converter.json;

import io.daobab.converter.JsonConverter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class JsonLocalDateTimeConverter implements JsonConverter<LocalDateTime> {

    final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddThh:mm:ss");

    @Override
    public String toJson(LocalDateTime obj) {
        return dateFormat.format(obj);
    }

    @Override
    public LocalDateTime fromJson(String json) {
         return LocalDateTime.parse(json);
    }
}
