package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterBooleanBased;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Standard converter for {@code Boolean} columns: read straight from the database, written as the SQL literals
 * {@code true}/{@code false}.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterBoolean extends TypeConverterBooleanBased<Boolean> {

    /**
     * Reads the {@code Boolean} straight from the result set (no conversion needed).
     */
    @Override
    public Boolean readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex);
    }

    /** Returns the database value unchanged. */
    @Override
    public Boolean convertReadingTarget(Boolean from) {
        return from;
    }

    /** Renders the value as {@code true} or {@code false}, or {@code null}. */
    @Override
    public String convertWritingTarget(Boolean to) {
        return to == null ? null : (Boolean.TRUE.equals(to) ? "true" : "false");
    }
}
