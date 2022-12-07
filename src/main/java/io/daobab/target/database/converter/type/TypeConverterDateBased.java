package io.daobab.target.database.converter.type;


import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeConverterDateBased<F> extends DatabaseTypeConverter<F, Date> {


    default Date readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getDate(columnIndex);
    }


}
