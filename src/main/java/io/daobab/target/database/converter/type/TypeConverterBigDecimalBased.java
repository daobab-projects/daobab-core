package io.daobab.target.database.converter.type;


import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeConverterBigDecimalBased<T> extends DatabaseTypeConverter<BigDecimal, T> {

    default BigDecimal readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getBigDecimal(columnIndex);
    }

}
