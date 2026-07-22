package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Base converter for columns read from the database as a {@link Timestamp}. Subclasses map that value to a
 * concrete Daobab column type {@code T}.
 *
 * @param <T> the Daobab column type produced from the {@code Timestamp} database value
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class TypeConverterTimestampBased<T> implements DatabaseTypeConverter<Timestamp, T> {


    /**
     * Reads the column as a {@link Timestamp}, or {@code null} when the column is SQL NULL.
     */
    public Timestamp readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getTimestamp(columnIndex);
    }


}
