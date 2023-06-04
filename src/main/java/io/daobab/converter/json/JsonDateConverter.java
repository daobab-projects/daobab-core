package io.daobab.converter.json;

import io.daobab.converter.JsonConverter;
import io.daobab.converter.JsonEscape;
import io.daobab.error.DaobabException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonDateConverter implements JsonConverter<Date> {

    final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm a z");

    @Override
    public String toJson(Date obj) {
        return dateFormat.format(obj);
    }

    @Override
    public Date fromJson(String json) {
        try {
            return dateFormat.parse(json);
        } catch (ParseException e) {
            throw new DaobabException("Date conversion failed.",e);
        }
    }
}
