package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

/**
 * Base converter for columns read from the database as an {@link OffsetDateTime}, i.e. a
 * {@code TIMESTAMP WITH TIME ZONE} whose offset is preserved via the JDBC 4.2
 * {@link ResultSet#getObject(int, Class)} accessor. Subclasses map that value to a concrete Daobab column
 * type {@code T}.
 *
 * @param <T> the Daobab column type produced from the {@code OffsetDateTime} database value
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class TypeConverterOffsetDateTimeBased<T> implements DatabaseTypeConverter<OffsetDateTime, T> {


    /**
     * Reads the column as an {@link OffsetDateTime}, preserving its offset, or {@code null} when the column
     * is SQL NULL.
     */
    public OffsetDateTime readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getObject(columnIndex, OffsetDateTime.class);
    }


}
