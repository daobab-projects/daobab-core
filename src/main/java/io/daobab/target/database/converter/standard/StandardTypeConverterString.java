package io.daobab.target.database.converter.standard;

import io.daobab.error.SqlInjectionDetected;
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
        return valueStringToSQL(to);
    }


    public static String valueStringToSQL(Object value) {
        StringBuilder sb = new StringBuilder();
        if (value == null) {
            return "";// do sth??
        }
        String valStr = value.toString();
        String valStrLower = value.toString().toLowerCase();
        if (valStr.contains(";")
                && (valStr.contains("'") || valStr.contains("\""))
                && (valStrLower.contains("table") || valStrLower.contains("insert") || valStrLower.contains("update") || valStrLower.contains("delete"))) {
            throw new SqlInjectionDetected(valStr);
        }
        sb.append(value.toString().replace("'", "''"));

        sb.append("'");
        return "'" + sb;
    }
}
