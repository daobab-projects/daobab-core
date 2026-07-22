package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterStringBased;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Standard converter for {@code String} columns: read straight from the database, written as a quoted SQL
 * string literal.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterString extends TypeConverterStringBased<String> {

    /**
     * Renders a value as a SQL string literal, escaping the apostrophes (doubling {@code '} into {@code ''}).
     * <p>
     * No SQL injection detection is needed anymore: regular query values are bound as PreparedStatement
     * parameters, and the remaining inline usages are escaped here.
     *
     * @param value the value to render (its {@code toString()} is used); {@code null} yields an empty string
     * @return the quoted, escaped SQL literal
     */
    public static String valueStringToSQL(Object value) {
        if (value == null) {
            return "";// do sth??
        }
        return "'" + value.toString().replace("'", "''") + "'";
    }

    /**
     * Reads the {@code String} straight from the result set (no conversion needed).
     */
    @Override
    public String readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex);
    }

    /** Returns the database value unchanged. */
    @Override
    public String convertReadingTarget(String from) {
        return from;
    }

    /** Renders the value as a quoted SQL literal (see {@link #valueStringToSQL(Object)}). */
    @Override
    public String convertWritingTarget(String to) {
        return valueStringToSQL(to);
    }
}
