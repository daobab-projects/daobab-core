package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeConverterBooleanBased<T> extends DatabaseTypeConverter<Boolean, T> {


    default Boolean readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getBoolean(columnIndex);
    }


}
