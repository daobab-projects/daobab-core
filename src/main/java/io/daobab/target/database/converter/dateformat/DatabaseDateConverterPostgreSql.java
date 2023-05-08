package io.daobab.target.database.converter.dateformat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class DatabaseDateConverterPostgreSql implements DatabaseDateConverter {


    @Override
    public String toDatabaseDate(Date value) {
        return " STR_TO_DATE('" + dateFormat.format(value) + "', '%Y-%m-%d %H:%i:%s')";
    }

    @Override
    public String toDatabaseTimestamp(Date value) {
        return " to_date('" + timeStampFormat.format(value) + "', '%Y-%m-%d %H:%i:%s')";
    }

    @Override
    public String toDatabaseTime(Date value) {
        return " STR_TO_DATE('" + timeFormat.format(value) + "', '%H:%i:%s')";
    }

    @Override
    public String toDatabaseLocalTime(LocalTime value) {
        return (String.format(" STR_TO_DATE('%s:%s:%s',", value.getHour(), value.getMinute(), value.getSecond())) + "'%H:%i:%s') ";
    }

    @Override
    public String toDatabaseLocalDate(LocalDate value) {
        return (String.format(" STR_TO_DATE('%s-%s-%s',", value.getYear(), value.getMonth(), value.getDayOfMonth())) + "'%Y-%m-%d') ";

    }

    @Override
    public String toDatabaseLocalDateTime(LocalDateTime value) {
        if (value == null) {
            return null;
        }

        return (String.format(" STR_TO_DATE('%s-%s-%s %s:%s:%s',", value.getYear(), value.getMonth(), value.getDayOfMonth(), value.getHour(), value.getMinute(), value.getSecond())) + "'%Y-%m-%d '%H:%i:%s') ";
    }

    @Override
    public String toDatabaseZonedDateTime(ZonedDateTime value) {
        if (value == null) {
            return null;
        }
        return (String.format(" STR_TO_DATE('%s-%s-%s %s:%s:%s',", value.getYear(), value.getMonth(), value.getDayOfMonth(), value.getHour(), value.getMinute(), value.getSecond())) + "'%Y-%m-%d '%H:%i:%s') ";
    }
}
