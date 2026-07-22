package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Base converter for columns read from the database as a {@code Short}. Subclasses map that value to a
 * concrete Daobab column type {@code T}.
 *
 * @param <T> the Daobab column type produced from the {@code Short} database value
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class TypeConverterShortBased<T> implements DatabaseTypeConverter<Short, T> {


    /**
     * Reads the column as a {@code short}, returned boxed, or {@code null} when the column is SQL NULL.
     */
    public Short readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        var rv = rs.getShort(columnIndex);
        if (rs.wasNull()) {
            return null;
        }
        return rv;
    }


}
