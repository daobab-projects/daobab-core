package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterStringBased;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StandardTypeConverterString implements TypeConverterStringBased<String> {

    @Override
    public String readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex);
    }

    @Override
    public String convertReadingTarget(String from) {
        return from;
    }

    @Override
    public String convertWritingTarget(String to) {
        return to;
    }
}
