package io.daobab.converter.json.type;

import io.daobab.converter.JsonConverter;
import io.daobab.error.DaobabException;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonTimestampConverter implements JsonConverter<Timestamp> {

    final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm a z");

    @Override
    public String toJson(Timestamp obj) {
        return obj.toString();
    }

    @Override
    public Timestamp fromJson(String json) {
        return null;
//        try {
//            return dateFormat.parse(json);
//        } catch (ParseException e) {
//            throw new DaobabException("Date conversion failed.",e);
//        }
    }
}
