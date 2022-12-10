package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeConverterDoubleBased<T> extends DatabaseTypeConverter<Double, T> {


    default Double readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getDouble(columnIndex);
    }


}
