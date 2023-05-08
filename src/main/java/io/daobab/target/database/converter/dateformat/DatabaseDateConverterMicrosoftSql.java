package io.daobab.target.database.converter.dateformat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
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


    @Override
    public String toDatabaseLocalTime(LocalTime value) {
        if (value == null) {
            return null;
        }
        return String.format(" convert(DATETIME,'%s:%s:%s')", value.getHour(), value.getMinute(), value.getSecond());
    }

    @Override
    public String toDatabaseLocalDate(LocalDate value) {
        if (value == null) {
            return null;
        }
        return String.format(" convert(DATETIME,'%s:%s:%s')", value.getYear(), value.getMonth(), value.getDayOfMonth());

    }

    @Override
    public String toDatabaseLocalDateTime(LocalDateTime value) {
        if (value == null) {
            return null;
        }
        return String.format(" convert(DATETIME,'%s-%s-%s %s:%s:%s')", value.getYear(), value.getMonth(), value.getDayOfMonth(), value.getHour(), value.getMinute(), value.getSecond());
    }


    @Override
    public String toDatabaseZonedDateTime(ZonedDateTime value) {
        if (value == null) {
            return null;
        }
        return String.format(" convert(DATETIME,'%s-%s-%s %s:%s:%s')", value.getYear(), value.getMonth(), value.getDayOfMonth(), value.getHour(), value.getMinute(), value.getSecond());
    }
}
