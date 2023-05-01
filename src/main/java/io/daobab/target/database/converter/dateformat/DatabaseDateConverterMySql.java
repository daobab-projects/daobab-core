package io.daobab.target.database.converter.dateformat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
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

    @Override
    public String toDatabaseLocalTime(LocalTime value) {
        if (value == null) {
            return null;
        }
        return String.format("%s:%s:%s", value.getHour(), value.getMinute(), value.getSecond());
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
        return String.format("'%s-%s-%s %s:%s:%s'", value.getYear(), value.getMonth(), value.getDayOfMonth(), value.getHour(), value.getMinute(), value.getSecond());
    }
}
