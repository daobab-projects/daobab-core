package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterLongBased;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Standard converter for opaque object columns, handled through their {@code Long} representation: read
 * straight from the database and written as decimal text.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterObject extends TypeConverterLongBased<Long> {

    /**
     * Reads the value straight from the result set (no conversion needed).
     */
    @Override
    public Long readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex);
    }

    /** Returns the database value unchanged. */
    @Override
    public Long convertReadingTarget(Long from) {
        return from;
    }

    /** Renders the value as its decimal text, or {@code null}. */
    @Override
    public String convertWritingTarget(Long to) {
        return to == null ? null : String.valueOf(to);
    }
}
