package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterFloatBased;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StandardTypeConverterFloat implements TypeConverterFloatBased<Float> {

    @Override
    public Float readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex);
    }

    @Override
    public Float convertReadingTarget(Float from) {
        return from;
    }

    @Override
    public String convertWritingTarget(Float to) {
        return to == null ? null : String.valueOf(to);
    }
}
