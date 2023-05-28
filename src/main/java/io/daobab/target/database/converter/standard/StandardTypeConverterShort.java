package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterShortBased;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StandardTypeConverterShort extends TypeConverterShortBased<Short> {

    @Override
    public Short readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex);
    }

    @Override
    public Short convertReadingTarget(Short from) {
        return from;
    }

    @Override
    public String convertWritingTarget(Short to) {
        return to == null ? null : String.valueOf(to);
    }
}
