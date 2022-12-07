package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeConverterStringBased<F> extends DatabaseTypeConverter<F, String> {


    default String readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getString(columnIndex);
    }


}
