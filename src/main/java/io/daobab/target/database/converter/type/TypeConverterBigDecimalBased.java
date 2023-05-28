package io.daobab.target.database.converter.type;


import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class TypeConverterBigDecimalBased<T> implements DatabaseTypeConverter<BigDecimal, T> {

    public BigDecimal readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getBigDecimal(columnIndex);
    }

}
