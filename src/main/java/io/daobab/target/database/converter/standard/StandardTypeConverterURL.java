package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterUrlBased;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Standard converter for {@link URL} columns: read straight from the database, written and bound as their
 * string form.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterURL extends TypeConverterUrlBased<URL> {

    /**
     * Reads the {@link URL} straight from the result set (no conversion needed).
     */
    @Override
    public URL readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex);
    }

    /** Returns the database value unchanged. */
    @Override
    public URL convertReadingTarget(URL from) {
        return from;
    }

    /** Renders the {@link URL} as its string form, or {@code null}. */
    @Override
    public String convertWritingTarget(URL to) {
        return to == null ? null : String.valueOf(to);
    }

    /** Binds the {@link URL} as its string form, or {@code null}. */
    @Override
    public Object convertWritingParameter(URL to) {
        return to == null ? null : String.valueOf(to);
    }
}
