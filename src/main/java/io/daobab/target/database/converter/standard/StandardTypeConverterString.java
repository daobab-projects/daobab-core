package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterStringBased;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StandardTypeConverterString extends TypeConverterStringBased<String> {

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
        return valueStringToSQL(to);
    }


    /**
     * Renders a string as a SQL literal, escaping the apostrophes.
     * No SQL injection detection is needed anymore: regular query values are bound
     * as PreparedStatement parameters, and the remaining inline usages are escaped here.
     */
    public static String valueStringToSQL(Object value) {
        if (value == null) {
            return "";// do sth??
        }
        return "'" + value.toString().replace("'", "''") + "'";
    }
}
