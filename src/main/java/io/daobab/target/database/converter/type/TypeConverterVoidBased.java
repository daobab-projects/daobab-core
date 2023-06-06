package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class TypeConverterVoidBased<T> implements DatabaseTypeConverter<Void, T> {

    public Void readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return null;
    }


}
