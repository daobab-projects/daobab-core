package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;

public abstract class TypeConverterSqlXmlBased<T> implements DatabaseTypeConverter<SQLXML, T> {


    public SQLXML readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getSQLXML(columnIndex);
    }


}
