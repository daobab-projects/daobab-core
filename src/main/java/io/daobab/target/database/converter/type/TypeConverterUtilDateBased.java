package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public interface TypeConverterUtilDateBased<F> extends DatabaseTypeConverter<F, Date> {


    default Date readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getDate(columnIndex);
    }


}
