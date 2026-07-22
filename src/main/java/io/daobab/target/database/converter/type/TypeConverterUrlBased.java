package io.daobab.target.database.converter.type;


import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Base converter for columns read from the database as a {@link URL}. Subclasses map that value to a
 * concrete Daobab column type {@code T}.
 *
 * @param <T> the Daobab column type produced from the {@code URL} database value
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class TypeConverterUrlBased<T> implements DatabaseTypeConverter<URL, T> {


    /**
     * Reads the column as a {@link URL}, or {@code null} when the column is SQL NULL.
     */
    public URL readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getURL(columnIndex);
    }


}
