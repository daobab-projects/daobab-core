package io.daobab.target.database.converter.dateformat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
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

    @Override
    public String toDatabaseLocalTime(LocalTime value) {
        return String.format(" TO_DATE('%s:%s:%s','HH24:MI:SS') ", value.getHour(), value.getMinute(), value.getSecond());
    }

    @Override
    public String toDatabaseLocalDate(LocalDate value) {
        return String.format(" TO_DATE(''%s:%s:%s'','YYYY-MM-DD') ", value.getYear(), value.getMonth(), value.getDayOfMonth());
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
