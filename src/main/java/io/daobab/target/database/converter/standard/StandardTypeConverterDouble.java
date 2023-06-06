package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterDoubleBased;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StandardTypeConverterDouble extends TypeConverterDoubleBased<Double> {

    @Override
    public Double readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex);
    }

    @Override
    public Double convertReadingTarget(Double from) {
        return from;
    }

    @Override
    public String convertWritingTarget(Double to) {
        return to == null ? null : String.valueOf(to);
    }
}
