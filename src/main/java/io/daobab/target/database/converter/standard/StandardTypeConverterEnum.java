package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterStringBased;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Standard converter for enum columns stored by their {@linkplain Enum#name() name}: the database {@code String}
 * is resolved to the matching enum constant on reading and written back as a quoted name.
 *
 * @param <E> the enum type
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings("rawtypes")
public class StandardTypeConverterEnum<E extends Enum> extends TypeConverterStringBased<E> {

    private final Class<E> enumClass;

    /**
     * @param enumClass the enum type whose constants back the column
     */
    public StandardTypeConverterEnum(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    /**
     * Reads the stored name and resolves it to the enum constant, or {@code null} when null/empty.
     */
    @Override
    public E readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        String value = readFromResultSet(rs, columnIndex);
        if (value == null || value.isEmpty()) return null;
        return convertReadingTarget(value);
    }

    /** Resolves the name to its enum constant. */
    @SuppressWarnings("unchecked")
    @Override
    public E convertReadingTarget(String from) {
        return (E) Enum.valueOf(enumClass, from);
    }

    /** Renders the constant's name as a quoted SQL literal, or {@code null}. */
    @Override
    public String convertWritingTarget(Enum to) {
        return to == null ? null : "'" + to + "'";
    }

    /** Binds the constant's name, or {@code null}. */
    @Override
    public Object convertWritingParameter(Enum to) {
        return to == null ? null : to.toString();
    }
}
