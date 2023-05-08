package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeConverterLongBased<T> extends DatabaseTypeConverter<Long, T> {


    default Long readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getLong(columnIndex);
    }


}
