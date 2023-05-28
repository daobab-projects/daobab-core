package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterByteArrayBased;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StandardTypeConverterBytes extends TypeConverterByteArrayBased<byte[]> {

    @Override
    public byte[] readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex);
    }

    @Override
    public byte[] convertReadingTarget(byte[] from) {
        return from;
    }

    @Override
    public String convertWritingTarget(byte[] to) {
        return to == null ? null : "?";
    }

    @Override
    public boolean needParameterConversion() {
        return true;
    }
}
