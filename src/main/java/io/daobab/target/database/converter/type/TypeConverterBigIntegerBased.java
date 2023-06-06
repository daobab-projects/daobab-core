package io.daobab.target.database.converter.type;


import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class TypeConverterBigIntegerBased<T> implements DatabaseTypeConverter<BigInteger, T> {

    public BigInteger readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getBigDecimal(columnIndex).toBigInteger();
    }

}
