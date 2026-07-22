package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

/**
 * Base converter for columns read from the database as a {@link Time}. Subclasses map that value to a
 * concrete Daobab column type {@code T}.
 *
 * @param <T> the Daobab column type produced from the {@code Time} database value
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class TypeConverterTimeBased<T> implements DatabaseTypeConverter<Time, T> {


    /**
     * Reads the column as a {@link Time}, or {@code null} when the column is SQL NULL.
     */
    public Time readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getTime(columnIndex);
    }


}
