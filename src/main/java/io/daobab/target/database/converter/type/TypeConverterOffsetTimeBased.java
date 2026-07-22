package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetTime;

/**
 * Base converter for columns read from the database as an {@link OffsetTime}, i.e. a
 * {@code TIME WITH TIME ZONE} whose offset is preserved via the JDBC 4.2
 * {@link ResultSet#getObject(int, Class)} accessor. Subclasses map that value to a concrete Daobab column
 * type {@code T}.
 *
 * @param <T> the Daobab column type produced from the {@code OffsetTime} database value
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class TypeConverterOffsetTimeBased<T> implements DatabaseTypeConverter<OffsetTime, T> {


    /**
     * Reads the column as an {@link OffsetTime}, preserving its offset, or {@code null} when the column
     * is SQL NULL.
     */
    public OffsetTime readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getObject(columnIndex, OffsetTime.class);
    }


}
