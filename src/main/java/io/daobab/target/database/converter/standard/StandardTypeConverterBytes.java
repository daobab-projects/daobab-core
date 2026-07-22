package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterByteArrayBased;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Standard converter for {@code byte[]} (BLOB / VARBINARY) columns: read straight from the database and always
 * bound as a PreparedStatement parameter rather than inlined.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterBytes extends TypeConverterByteArrayBased<byte[]> {

    /**
     * Reads the {@code byte[]} straight from the result set (no conversion needed).
     */
    @Override
    public byte[] readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex);
    }

    /** Returns the database value unchanged. */
    @Override
    public byte[] convertReadingTarget(byte[] from) {
        return from;
    }

    /** Renders a {@code ?} placeholder (the bytes are always bound as a parameter), or {@code null}. */
    @Override
    public String convertWritingTarget(byte[] to) {
        return to == null ? null : "?";
    }

    /** Binary data is always bound as a PreparedStatement parameter. */
    @Override
    public boolean needParameterConversion() {
        return true;
    }
}
