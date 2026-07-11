package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetTime;

/**
 * Reads a {@code TIME WITH TIME ZONE} column preserving its offset, via the JDBC 4.2
 * {@link ResultSet#getObject(int, Class)} accessor.
 */
public abstract class TypeConverterOffsetTimeBased<T> implements DatabaseTypeConverter<OffsetTime, T> {


    public OffsetTime readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getObject(columnIndex, OffsetTime.class);
    }


}
