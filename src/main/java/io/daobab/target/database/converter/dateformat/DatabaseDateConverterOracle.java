package io.daobab.target.database.converter.dateformat;

import java.util.Date;

public class DatabaseDateConverterOracle implements DatabaseDateConverter {

    @Override
    public String toDate(Date value) {
        if (value instanceof java.sql.Timestamp) {
            return toTimestampDate(value);
        }
        return toSQLDate(value);
    }

    @Override
    public String toSQLDate(Date value) {
        return " TO_DATE('" + dateFormat.format(value) + "','YYYY-MM-DD HH24:MI:SS') ";
    }

    @Override
    public String toTimestampDate(Date value) {
        return " TO_DATE('" + timeStampFormat.format(value) + "','YYYY-MM-DD HH24:MI:SS') ";
    }

    @Override
    public String toTimeDate(Date value) {
        return " TO_DATE('" + timeFormat.format(value) + "','HH24:MI:SS') ";
    }
}
