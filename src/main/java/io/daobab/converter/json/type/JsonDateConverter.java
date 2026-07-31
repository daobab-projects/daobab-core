package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * JSON converter for {@link java.util.Date}: rendered as a {@link java.time.LocalDateTime} in the
 * system default zone (millisecond precision) and read back into that zone.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
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
