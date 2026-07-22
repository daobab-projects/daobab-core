package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterByteBased;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Standard converter for {@code Byte} columns: read straight from the database, written as their decimal text.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterByte extends TypeConverterByteBased<Byte> {

    /**
     * Reads the {@code Byte} straight from the result set (no conversion needed).
     */
    @Override
    public Byte readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex);
    }

    /** Returns the database value unchanged. */
    @Override
    public Byte convertReadingTarget(Byte from) {
        return from;
    }

    /** Renders the value as its decimal text, or {@code null}. */
    @Override
    public String convertWritingTarget(Byte to) {
        return to == null ? null : String.valueOf(to);
    }
}
