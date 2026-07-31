package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;
import io.daobab.error.DaobabException;

import java.sql.SQLException;
import java.sql.SQLXML;

/**
 * JSON converter for {@link SQLXML} columns - the counterpart of
 * {@link io.daobab.target.database.converter.type.TypeConverterSqlXmlBased}. The XML content is written as a
 * quoted, escaped JSON string (via {@link JsonStringConverter}). A {@code java.sql.SQLXML} is a driver-backed
 * handle that cannot be rebuilt from a string without a live database connection, so {@link #fromJson} is
 * unsupported.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JsonSqlXmlConverter extends JsonConverter<SQLXML> {

    private final JsonStringConverter stringConverter = new JsonStringConverter();

    @Override
    public void toJson(StringBuilder sb, SQLXML obj) {
        try {
            stringConverter.toJson(sb, obj.getString());
        } catch (SQLException e) {
            throw new DaobabException("Problem during Json conversion of SQLXML", e);
        }
    }

    @Override
    public SQLXML fromJson(String json) {
        throw new DaobabException("A java.sql.SQLXML cannot be rebuilt from JSON without a database connection");
    }
}
