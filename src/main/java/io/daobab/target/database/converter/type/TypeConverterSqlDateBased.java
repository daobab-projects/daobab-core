package io.daobab.target.database.converter.type;


import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Base converter for columns read from the database as a {@link java.sql.Date}. Subclasses map that value to a
 * concrete Daobab column type {@code T}.
 *
 * @param <T> the Daobab column type produced from the {@code java.sql.Date} database value
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class TypeConverterSqlDateBased<T> implements DatabaseTypeConverter<Date, T> {


    /**
     * Reads the column as a {@link java.sql.Date}, or {@code null} when the column is SQL NULL.
     */
    public Date readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getDate(columnIndex);
    }

}
