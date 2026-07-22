package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;

/**
 * Base converter for columns read from the database as a {@link SQLXML}. Subclasses map that value to a
 * concrete Daobab column type {@code T}.
 *
 * @param <T> the Daobab column type produced from the {@code SQLXML} database value
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class TypeConverterSqlXmlBased<T> implements DatabaseTypeConverter<SQLXML, T> {


    /**
     * Reads the column as a {@link SQLXML}, or {@code null} when the column is SQL NULL.
     */
    public SQLXML readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getSQLXML(columnIndex);
    }


}
