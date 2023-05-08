package io.daobab.target.database.converter.standard;

import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.converter.type.TypeConverterTimeBased;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

public class StandardTypeConverterTime implements TypeConverterTimeBased<Time> {

    private final DataBaseTarget target;

    public StandardTypeConverterTime(DataBaseTarget target) {
        this.target = target;
    }

    @Override
    public Time readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return readFromResultSet(rs, columnIndex);
    }

    @Override
    public Time convertReadingTarget(Time from) {
        return from;
    }

    @Override
    public String convertWritingTarget(Time to) {
        return target.getDatabaseDateConverter().toDatabaseTime(to);
    }
}
