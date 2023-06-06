package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterByteBased;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StandardTypeConverterByte extends TypeConverterByteBased<Byte> {

    @Override
    public Byte readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex);
    }

    @Override
    public Byte convertReadingTarget(Byte from) {
        return from;
    }

    @Override
    public String convertWritingTarget(Byte to) {
        return to == null ? null : String.valueOf(to);
    }
}
