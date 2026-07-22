package io.daobab.target.database.converter.type;


import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Base converter for columns read from the database as a JDBC {@link Array}. Subclasses map that value to a
 * concrete Daobab column type {@code T}.
 *
 * @param <T> the Daobab column type produced from the {@code Array} database value
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class TypeConverterArrayBased<T> implements DatabaseTypeConverter<Array, T> {


    /**
     * Reads the column as a JDBC {@link Array}, or {@code null} when the column is SQL NULL.
     */
    public Array readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getArray(columnIndex);
    }


}
