package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;
import io.daobab.converter.json.JsonConverterManager;
import io.daobab.error.DaobabException;

import java.lang.reflect.Array;
import java.sql.SQLException;

/**
 * JSON converter for {@link java.sql.Array} columns - the counterpart of
 * {@link io.daobab.target.database.converter.type.TypeConverterArrayBased}. The array elements are written as a
 * JSON array, each element serialized by its own registered type converter (falling back to a quoted, escaped
 * string). A {@code java.sql.Array} is a driver-backed handle that cannot be rebuilt from a string without a
 * live database connection, so {@link #fromJson} is unsupported.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JsonArrayConverter extends JsonConverter<java.sql.Array> {

    private final JsonStringConverter stringConverter = new JsonStringConverter();

    @Override
    public void toJson(StringBuilder sb, java.sql.Array obj) {
        Object elements;
        try {
            elements = obj.getArray();
        } catch (SQLException e) {
            throw new DaobabException("Problem during Json conversion of SQL Array", e);
        }
        sb.append("[");
        int length = elements == null ? 0 : Array.getLength(elements);
        for (int i = 0; i < length; i++) {
            appendElement(sb, Array.get(elements, i));
            if (i < length - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void appendElement(StringBuilder sb, Object element) {
        if (element == null) {
            sb.append("null");
            return;
        }
        JsonConverter converter = JsonConverterManager.INSTANCE.getTypeConverter(element.getClass()).orElse(null);
        if (converter != null) {
            converter.toJson(sb, element);
        } else {
            stringConverter.toJson(sb, element.toString());
        }
    }

    @Override
    public java.sql.Array fromJson(String json) {
        throw new DaobabException("A java.sql.Array cannot be rebuilt from JSON without a database connection");
    }
}
