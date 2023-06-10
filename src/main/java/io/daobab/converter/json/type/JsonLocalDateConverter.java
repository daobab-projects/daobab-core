package io.daobab.converter.json.type;

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
        return "\""+obj.toString()+"\"";
    }

    @Override
    public LocalDate fromJson(String json) {
         return LocalDate.parse(json);
    }
}
