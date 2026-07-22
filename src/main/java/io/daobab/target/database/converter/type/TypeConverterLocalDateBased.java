package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Base converter for columns read from the database as a {@link LocalDate} (a {@link java.sql.Date} converted
 * to it). Subclasses map that value to a concrete Daobab column type {@code T}.
 *
 * @param <T> the Daobab column type produced from the {@code LocalDate} database value
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class TypeConverterLocalDateBased<T> implements DatabaseTypeConverter<LocalDate, T> {

    /**
     * Reads the column as a {@link java.sql.Date} and converts it to a {@link LocalDate},
     * or {@code null} when the column is SQL NULL.
     */
    public LocalDate readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        java.sql.Date date = rs.getDate(columnIndex);
        return date == null ? null : date.toLocalDate();
    }


}
