package io.daobab.target.database.converter.type;


import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeConverterUrlBased<T> extends DatabaseTypeConverter<URL, T> {


    default URL readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getURL(columnIndex);
    }


}
