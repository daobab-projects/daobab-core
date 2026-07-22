package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterDoubleBased;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Standard converter for {@code Double} columns: read straight from the database, written as their decimal text.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterDouble extends TypeConverterDoubleBased<Double> {

    /**
     * Reads the {@code Double} straight from the result set (no conversion needed).
     */
    @Override
    public Double readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex);
    }

    /** Returns the database value unchanged. */
    @Override
    public Double convertReadingTarget(Double from) {
        return from;
    }

    /** Renders the value as its decimal text, or {@code null}. */
    @Override
    public String convertWritingTarget(Double to) {
        return to == null ? null : String.valueOf(to);
    }
}
