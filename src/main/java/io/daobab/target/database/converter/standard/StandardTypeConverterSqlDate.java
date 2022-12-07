package io.daobab.target.database.converter.standard;

import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.converter.type.TypeConverterSqlDateBased;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StandardTypeConverterSqlDate implements TypeConverterSqlDateBased<Date> {

    private final DataBaseTarget target;

    public StandardTypeConverterSqlDate(DataBaseTarget target) {
        this.target = target;
    }

    @Override
    public Date readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex);
    }

    @Override
    public Date convertReadingTarget(Date from) {
        return from;
    }

    @Override
    public String convertWritingTarget(Date to) {
        return target.getDatabaseDateConverter().toDate(to);
    }
}
