package io.daobab.target.database.converter.standard;

import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.converter.type.TypeConverterTimeBased;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;

public class StandardTypeConverterLocalTime extends TypeConverterTimeBased<LocalTime> {

    private final DataBaseTarget target;

    public StandardTypeConverterLocalTime(DataBaseTarget target) {
        this.target = target;
    }

    @Override
    public LocalTime readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        java.sql.Time sqlTime = readFromResultSet(rs, columnIndex);
        return sqlTime.toLocalTime();
    }

    @Override
    public LocalTime convertReadingTarget(Time from) {
        return from.toLocalTime();
    }

    @Override
    public String convertWritingTarget(LocalTime to) {
        return target.getDatabaseDateConverter().toDatabaseLocalTime(to);
    }
}
