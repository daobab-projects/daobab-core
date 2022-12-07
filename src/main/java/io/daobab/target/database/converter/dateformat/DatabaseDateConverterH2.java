package io.daobab.target.database.converter.dateformat;

import java.util.Date;

public class DatabaseDateConverterH2 implements DatabaseDateConverter {


    @Override
    public String toDate(Date value) {
        if (value instanceof java.sql.Timestamp) {
            return toTimestampDate(value);
        }
        return toSQLDate(value);
    }

    @Override
    public String toSQLDate(Date value) {
        return APOSTROPHE + dateFormat.format(value) + APOSTROPHE;
    }

    @Override
    public String toTimestampDate(Date value) {
        return APOSTROPHE + timeStampFormat.format(value) + APOSTROPHE;
    }

    @Override
    public String toTimeDate(Date value) {
        return APOSTROPHE + timeFormat.format(value) + APOSTROPHE;
    }
}
