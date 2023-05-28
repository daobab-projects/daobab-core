package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public abstract class TypeConverterUtilDateBased<T> implements DatabaseTypeConverter<Date, T> {


    public Date readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getDate(columnIndex);
    }


}
