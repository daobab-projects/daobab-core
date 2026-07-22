package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Base converter for columns read from the database as a {@code Byte}. Subclasses map that value to a
 * concrete Daobab column type {@code T}.
 *
 * @param <T> the Daobab column type produced from the {@code Byte} database value
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class TypeConverterByteBased<T> implements DatabaseTypeConverter<Byte, T> {


    /**
     * Reads the column as a {@code byte}, returned boxed, or {@code null} when the column is SQL NULL.
     */
    public Byte readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        var rv = rs.getByte(columnIndex);
        if (rs.wasNull()) {
            return null;
        }
        return rv;
    }


}
