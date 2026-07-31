package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.sql.Date;

/**
 * JSON converter for {@link java.sql.Date}: delegates to {@link JsonLocalDateConverter} through the
 * date's {@code toLocalDate()} form (quoted {@code yyyy-MM-dd}).
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JsonSqlDateConverter extends JsonConverter<Date> {

    private final JsonLocalDateConverter jsonLocalDateTimeConverter = new JsonLocalDateConverter();

    @Override
    public void toJson(StringBuilder sb, Date obj) {
        jsonLocalDateTimeConverter.toJson(sb, obj.toLocalDate());
    }

    @Override
    public Date fromJson(String json) {
        return Date.valueOf(jsonLocalDateTimeConverter.fromJson(json));
    }
}
