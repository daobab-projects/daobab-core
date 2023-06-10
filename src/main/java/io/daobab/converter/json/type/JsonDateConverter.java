package io.daobab.converter.json.type;

import io.daobab.converter.JsonConverter;
import io.daobab.error.DaobabException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class JsonDateConverter extends JsonConverter<Date> {

    private final JsonLocalDateTimeConverter jsonLocalDateTimeConverter=new JsonLocalDateTimeConverter();

    final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss.SSSZ");

    @Override
    public void toJson(StringBuilder sb, Date obj) {
        jsonLocalDateTimeConverter.toJson(sb, LocalDateTime.ofInstant(obj.toInstant(), ZoneId.systemDefault()));
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
