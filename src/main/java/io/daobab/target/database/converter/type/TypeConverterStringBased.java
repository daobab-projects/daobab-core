package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Base converter for columns read from the database as a {@code String}. Subclasses map that value to a
 * concrete Daobab column type {@code T}.
 *
 * @param <T> the Daobab column type produced from the {@code String} database value
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class TypeConverterStringBased<T> implements DatabaseTypeConverter<String, T> {


    /**
     * Reads the column as a {@code String}, or {@code null} when the column is SQL NULL.
     */
    public String readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getString(columnIndex);
    }


}
