package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterStringBased;

import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("rawtypes")
public class StandardTypeConverterEnum<E extends Enum> implements TypeConverterStringBased<E> {

    private final Class<E> enumClass;

    public StandardTypeConverterEnum(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public E readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        String value = readFromResultSet(rs, columnIndex);
        if (value == null || value.isEmpty()) return null;
        return convertReadingTarget(value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public E convertReadingTarget(String from) {
        return (E) Enum.valueOf(enumClass, from);
    }

    @Override
    public String convertWritingTarget(Enum to) {
        return to == null ? null : "'" + to + "'";
    }
}
