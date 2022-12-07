package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterIntegerBased;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StandardTypeConverterInteger implements TypeConverterIntegerBased<Integer> {

    @Override
    public Integer readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex);
    }

    @Override
    public Integer convertReadingTarget(Integer from) {
        return from;
    }

    @Override
    public String convertWritingTarget(Integer to) {
        return to == null ? null : String.valueOf(to);
    }
}
