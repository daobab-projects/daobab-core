package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Base converter for columns read from the database as a {@link java.util.Date} (a {@link java.sql.Date}
 * returned by the driver widens to it). Subclasses map that value to a concrete Daobab column type {@code T}.
 *
 * @param <T> the Daobab column type produced from the {@code java.util.Date} database value
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class TypeConverterUtilDateBased<T> implements DatabaseTypeConverter<Date, T> {


    /**
     * Reads the column as a date, or {@code null} when the column is SQL NULL.
     */
    public Date readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getDate(columnIndex);
    }


}
