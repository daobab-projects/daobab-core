package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class TypeConverterLongBased<T> implements DatabaseTypeConverter<Long, T> {


    public Long readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getLong(columnIndex);
    }


}
