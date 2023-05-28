package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public abstract class TypeConverterTimestampBased<T> implements DatabaseTypeConverter<Timestamp, T> {


    public Timestamp readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getTimestamp(columnIndex);
    }


}
