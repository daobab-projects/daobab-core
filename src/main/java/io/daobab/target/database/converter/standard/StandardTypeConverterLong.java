package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterLongBased;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StandardTypeConverterLong extends TypeConverterLongBased<Long> {

    @Override
    public Long readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex);
    }

    @Override
    public Long convertReadingTarget(Long from) {
        return from;
    }

    @Override
    public String convertWritingTarget(Long to) {
        return to == null ? null : String.valueOf(to);
    }
}
