package io.daobab.target.database.converter.standard;

import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.converter.type.TypeConverterUtilDateBased;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class StandardTypeConverterUtilDate implements TypeConverterUtilDateBased<Date> {

    private final DataBaseTarget target;

    public StandardTypeConverterUtilDate(DataBaseTarget target) {
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
        return target.getDatabaseDateConverter().toSQLDate(to);
    }
}
