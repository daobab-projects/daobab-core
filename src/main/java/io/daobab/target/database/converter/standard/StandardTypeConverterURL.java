package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterUrlBased;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StandardTypeConverterURL implements TypeConverterUrlBased<URL> {

    @Override
    public URL readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex);
    }

    @Override
    public URL convertReadingTarget(URL from) {
        return from;
    }

    @Override
    public String convertWritingTarget(URL to) {
        return to == null ? null : String.valueOf(to);
    }
}
