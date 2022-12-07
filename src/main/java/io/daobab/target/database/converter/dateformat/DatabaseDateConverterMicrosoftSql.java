package io.daobab.target.database.converter.dateformat;

import java.util.Date;

public class DatabaseDateConverterMicrosoftSql implements DatabaseDateConverter {


    @Override
    public String toDatabaseDate(Date value) {
        return " convert(DATETIME,'" + dateFormat.format(value) + "')";
    }

    @Override
    public String toDatabaseTimestamp(Date value) {
        return " convert(DATETIME,'" + timeStampFormat.format(value) + "')";
    }

    @Override
    public String toDatabaseTime(Date value) {
        return " convert(DATETIME,'" + timeFormat.format(value) + "')";
    }
}
