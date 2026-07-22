package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Base converter for columns read from the database as a {@code Boolean}. Subclasses map that value to a
 * concrete Daobab column type {@code T}.
 *
 * @param <T> the Daobab column type produced from the {@code Boolean} database value
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class TypeConverterBooleanBased<T> implements DatabaseTypeConverter<Boolean, T> {


    /**
     * Reads the column as a {@code boolean}, returned boxed, or {@code null} when the column is SQL NULL.
     */
    public Boolean readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        var rv = rs.getBoolean(columnIndex);
        if (rs.wasNull()) {
            return null;
        }
        return rv;
    }


}
