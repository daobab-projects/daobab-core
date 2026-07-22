package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterIntegerBased;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Standard converter for {@code Integer} columns: read straight from the database, written as their decimal text.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterInteger extends TypeConverterIntegerBased<Integer> {

    /**
     * Reads the {@code Integer} straight from the result set (no conversion needed).
     */
    @Override
    public Integer readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex);
    }

    /** Returns the database value unchanged. */
    @Override
    public Integer convertReadingTarget(Integer from) {
        return from;
    }

    /** Renders the value as its decimal text, or {@code null}. */
    @Override
    public String convertWritingTarget(Integer to) {
        return to == null ? null : String.valueOf(to);
    }
}
