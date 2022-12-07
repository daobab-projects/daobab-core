package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterBooleanBased;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StandardTypeConverterBoolean implements TypeConverterBooleanBased<Boolean> {

    @Override
    public Boolean readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex);
    }

    @Override
    public Boolean convertReadingTarget(Boolean from) {
        return from;
    }

    @Override
    public String convertWritingTarget(Boolean to) {
        return to == null ? null : String.valueOf(to);
    }
}
