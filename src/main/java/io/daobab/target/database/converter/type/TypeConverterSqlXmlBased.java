package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;

public interface TypeConverterSqlXmlBased<F> extends DatabaseTypeConverter<F, SQLXML> {


    default SQLXML readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getSQLXML(columnIndex);
    }


}
