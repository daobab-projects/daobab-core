package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public interface TypeConverterTimestampBased<F> extends DatabaseTypeConverter<F, Timestamp> {


    default Timestamp readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getTimestamp(columnIndex);
    }


}
