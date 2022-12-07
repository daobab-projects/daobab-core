package io.daobab.target.database.converter.dateformat;

import java.util.Date;

public class DatabaseDateConverterH2 implements DatabaseDateConverter {

    @Override
    public String toDatabaseDate(Date value) {
        return APOSTROPHE + dateFormat.format(value) + APOSTROPHE;
    }

    @Override
    public String toDatabaseTimestamp(Date value) {
        return APOSTROPHE + timeStampFormat.format(value) + APOSTROPHE;
    }

    @Override
    public String toDatabaseTime(Date value) {
        return APOSTROPHE + timeFormat.format(value) + APOSTROPHE;
    }
}
