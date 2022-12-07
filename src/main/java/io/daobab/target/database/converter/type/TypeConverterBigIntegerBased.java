package io.daobab.target.database.converter.type;


import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeConverterBigIntegerBased<F> extends DatabaseTypeConverter<F, BigInteger> {

    default BigInteger readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getBigDecimal(columnIndex).toBigInteger();
    }

}
