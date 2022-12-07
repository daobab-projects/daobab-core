package io.daobab.target.database.converter.dateformat;

import java.util.Date;

public class DatabaseDateConverterMicrosoftSql implements DatabaseDateConverter {

    @Override
    public String toDate(Date value) {
        if (value instanceof java.sql.Timestamp) {
            return toTimestampDate(value);
        }
        return toSQLDate(value);
    }

    @Override
    public String toSQLDate(Date value) {
        return " convert(DATETIME,'" + dateFormat.format(value) + "')";
    }

    @Override
    public String toTimestampDate(Date value) {
        return " convert(DATETIME,'" + timeStampFormat.format(value) + "')";
    }

    @Override
    public String toTimeDate(Date value) {
        return " convert(DATETIME,'" + timeFormat.format(value) + "')";
    }
}
