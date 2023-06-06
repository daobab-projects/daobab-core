package io.daobab.target.database.converter.type;


import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class TypeConverterSqlDateBased<T> implements DatabaseTypeConverter<Date, T> {


    public Date readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getDate(columnIndex);
    }

}
