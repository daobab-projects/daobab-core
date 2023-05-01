package io.daobab.target.database.converter.dateformat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
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

    @Override
    public String toDatabaseLocalTime(LocalTime value) {
        if (value == null) {
            return null;
        }
        return String.format("'%s:%s:%d'", value.getHour(), value.getMinute(), value.getSecond());
    }

    @Override
    public String toDatabaseLocalDate(LocalDate value) {
        if (value == null) {
            return null;
        }
        return String.format("'%s-%s-%s'", value.getYear(), value.getMonth(), value.getDayOfMonth());
    }

    @Override
    public String toDatabaseLocalDateTime(LocalDateTime value) {
        if (value == null) {
            return null;
        }
        return String.format("'%s-%s-%s %s:%s:%s'", value.getYear(), value.getMonth(), value.getDayOfMonth(), value.getHour(), value.getMinute(), value.getSecond());
    }

    @Override
    public String toDatabaseZonedDateTime(ZonedDateTime value) {
        if (value == null) {
            return null;
        }
        return String.format("'%s-%s-%s %s:%s:%s GMT'", value.getYear(), value.getMonth(), value.getDayOfMonth(), value.getHour(), value.getMinute(), value.getSecond());
    }
}
