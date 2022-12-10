package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeConverterVoidBased<T> extends DatabaseTypeConverter<Void, T> {

    default Void readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return null;
    }


}
