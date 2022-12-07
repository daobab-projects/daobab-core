package io.daobab.target.database.converter.dateformat;

import java.util.Date;

public class DatabaseDateConverterMySql implements DatabaseDateConverter {


    @Override
    public String toDatabaseDate(Date value) {
        return " STR_TO_DATE('" + dateFormat.format(value) + "', '%Y-%m-%d %H:%i:%s')";
    }

    @Override
    public String toDatabaseTimestamp(Date value) {
        return " STR_TO_DATE('" + timeStampFormat.format(value) + "', '%Y-%m-%d %H:%i:%s')";
    }

    @Override
    public String toDatabaseTime(Date value) {
        return " STR_TO_DATE('" + timeFormat.format(value) + "', '%H:%i:%s')";
    }
}
