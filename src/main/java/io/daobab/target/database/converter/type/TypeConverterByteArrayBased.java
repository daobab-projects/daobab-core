package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Base converter for columns read from the database as a {@code byte[]} (BLOB / VARBINARY). Subclasses map that
 * value to a concrete Daobab column type {@code T}.
 *
 * @param <T> the Daobab column type produced from the {@code byte[]} database value
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class TypeConverterByteArrayBased<T> implements DatabaseTypeConverter<byte[], T> {


    /**
     * Reads the column as a {@code byte[]}, or {@code null} when the column is SQL NULL.
     */
    public byte[] readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getBytes(columnIndex);
    }


}
