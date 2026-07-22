package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Base converter for columns read from the database as a {@code Double}. Subclasses map that value to a
 * concrete Daobab column type {@code T}.
 *
 * @param <T> the Daobab column type produced from the {@code Double} database value
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class TypeConverterDoubleBased<T> implements DatabaseTypeConverter<Double, T> {


    /**
     * Reads the column as a {@code double}, returned boxed, or {@code null} when the column is SQL NULL.
     */
    public Double readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        var rv = rs.getDouble(columnIndex);
        if (rs.wasNull()) {
            return null;
        }
        return rv;
    }


}
