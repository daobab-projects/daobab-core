package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterFloatBased;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Standard converter for {@code Float} columns: read straight from the database, written as their decimal text.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterFloat extends TypeConverterFloatBased<Float> {

    /**
     * Reads the {@code Float} straight from the result set (no conversion needed).
     */
    @Override
    public Float readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex);
    }

    /** Returns the database value unchanged. */
    @Override
    public Float convertReadingTarget(Float from) {
        return from;
    }

    /** Renders the value as its decimal text, or {@code null}. */
    @Override
    public String convertWritingTarget(Float to) {
        return to == null ? null : String.valueOf(to);
    }
}
