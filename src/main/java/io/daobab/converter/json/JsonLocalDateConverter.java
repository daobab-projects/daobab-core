package io.daobab.converter.json;

import io.daobab.converter.JsonConverter;
import io.daobab.error.DaobabException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class JsonLocalDateConverter implements JsonConverter<LocalDate> {

    final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public String toJson(LocalDate obj) {
        return dateFormat.format(obj);
    }

    @Override
    public LocalDate fromJson(String json) {
         return LocalDate.parse(json);
    }
}
