package io.daobab.target.database.converter.dateformat;

import java.util.Date;

public class DatabaseDateConverterMySql implements DatabaseDateConverter {


    @Override
    public String toDate(Date value) {
        if (value instanceof java.sql.Timestamp) {
            return toTimestampDate(value);
        }
        return toSQLDate(value);
    }

    @Override
    public String toSQLDate(Date value) {
        return " STR_TO_DATE('" + dateFormat.format(value) + "', '%Y-%m-%d %H:%i:%s')";
    }

    @Override
    public String toTimestampDate(Date value) {
        return " STR_TO_DATE('" + timeStampFormat.format(value) + "', '%Y-%m-%d %H:%i:%s')";
    }

    @Override
    public String toTimeDate(Date value) {
        return " STR_TO_DATE('" + timeFormat.format(value) + "', '%H:%i:%s')";
    }
}
