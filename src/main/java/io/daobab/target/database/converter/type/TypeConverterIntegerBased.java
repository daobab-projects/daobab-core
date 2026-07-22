package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Base converter for columns read from the database as an {@code Integer}. Subclasses map that value to a
 * concrete Daobab column type {@code T}.
 *
 * @param <T> the Daobab column type produced from the {@code Integer} database value
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class TypeConverterIntegerBased<T> implements DatabaseTypeConverter<Integer, T> {


    /**
     * Reads the column as an {@code int}, returned boxed, or {@code null} when the column is SQL NULL.
     */
    public Integer readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        int rv = rs.getInt(columnIndex);
        if (rs.wasNull()) {
            return null;
        }
        return rv;
    }


}
