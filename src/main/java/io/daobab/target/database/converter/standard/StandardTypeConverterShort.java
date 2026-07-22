package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterShortBased;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Standard converter for {@code Short} columns: read straight from the database, written as their decimal text.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterShort extends TypeConverterShortBased<Short> {

    /**
     * Reads the {@code Short} straight from the result set (no conversion needed).
     */
    @Override
    public Short readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex);
    }

    /** Returns the database value unchanged. */
    @Override
    public Short convertReadingTarget(Short from) {
        return from;
    }

    /** Renders the value as its decimal text, or {@code null}. */
    @Override
    public String convertWritingTarget(Short to) {
        return to == null ? null : String.valueOf(to);
    }
}
