package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

/**
 * Reads a {@code TIMESTAMP WITH TIME ZONE} column preserving its offset, via the JDBC 4.2
 * {@link ResultSet#getObject(int, Class)} accessor.
 */
public abstract class TypeConverterOffsetDateTimeBased<T> implements DatabaseTypeConverter<OffsetDateTime, T> {


    public OffsetDateTime readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getObject(columnIndex, OffsetDateTime.class);
    }


}
