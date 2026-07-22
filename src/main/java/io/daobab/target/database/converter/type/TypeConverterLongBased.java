package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Base converter for columns read from the database as a {@code Long}. Subclasses map that value to a
 * concrete Daobab column type {@code T}.
 *
 * @param <T> the Daobab column type produced from the {@code Long} database value
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class TypeConverterLongBased<T> implements DatabaseTypeConverter<Long, T> {


    /**
     * Reads the column as a {@code long}, returned boxed, or {@code null} when the column is SQL NULL.
     */
    public Long readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        var rv = rs.getLong(columnIndex);
        if (rs.wasNull()) {
            return null;
        }
        return rv;
    }


}
