package io.daobab.target.database.converter.dateformat;

import java.util.Date;

public class DatabaseDateConverterOracle implements DatabaseDateConverter {

    @Override
    public String toDatabaseDate(Date value) {
        return " TO_DATE('" + dateFormat.format(value) + "','YYYY-MM-DD HH24:MI:SS') ";
    }

    @Override
    public String toDatabaseTimestamp(Date value) {
        return " TO_DATE('" + timeStampFormat.format(value) + "','YYYY-MM-DD HH24:MI:SS') ";
    }

    @Override
    public String toDatabaseTime(Date value) {
        return " TO_DATE('" + timeFormat.format(value) + "','HH24:MI:SS') ";
    }
}
