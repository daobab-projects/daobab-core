package io.daobab.target.database.converter.dateformat;

import java.text.SimpleDateFormat;
import java.util.Date;

public interface DatabaseDateConverter {

    String APOSTROPHE = "'";
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    String toDatabaseDate(Date value);

    String toDatabaseTimestamp(Date value);

    String toDatabaseTime(Date value);
}
