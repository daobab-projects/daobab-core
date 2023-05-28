package io.daobab.target.database.converter.type;


import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class TypeConverterUrlBased<T> implements DatabaseTypeConverter<URL, T> {


    public URL readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getURL(columnIndex);
    }


}
