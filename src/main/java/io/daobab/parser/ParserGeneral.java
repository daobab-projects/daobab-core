package io.daobab.parser;

import io.daobab.error.ParserException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
@SuppressWarnings("unused")
public interface ParserGeneral {

    default BigInteger toBigInteger(Number val) {
        return ParserNumber.toBigInteger(val);
    }

    default BigDecimal toBD(Number val) {
        return ParserNumber.toBigDecimal(val);
    }

    default BigDecimal toBigDecimal(Number val) {
        return ParserNumber.toBigDecimal(val);
    }

    default Boolean toBooleanBig(Number val, Boolean inCaseOfNull) {
        return ParserNumber.toBoolean(val, inCaseOfNull);
    }

    default boolean toBooleanSmall(Number val, boolean inCaseOfNull) {
        return ParserNumber.toBooleanSmall(val, inCaseOfNull);
    }

    default Double toDouble(Number val) {
        return ParserNumber.toDouble(val);
    }

    default Float toFloat(Number val) {
        return ParserNumber.toFloat(val);
    }

    default Short toShort(Number val) {
        return ParserNumber.toShort(val);
    }

    default Integer toInteger(Number val) {
        return ParserNumber.toInteger(val);
    }

    default Integer toInteger(String val) {
        return ParserString.toInteger(val);
    }

    default Long toLong(String val) {
        return ParserString.toLong(val);
    }

    default BigDecimal toBigDecimal(String val) {
        return ParserString.toBigDecimal(val);
    }

    default Double toDouble(String val) {
        return ParserString.toDouble(val);
    }

    default Float toFloat(String val) {
        return ParserString.toFloat(val);
    }

    default Long toLong(Number val) {
        return ParserNumber.toLong(val);
    }

    default String toString(Number val) {
        return ParserNumber.toString(val);
    }

    default Date toDate(String val, String pattern) {
        if (val == null) return null;
        SimpleDateFormatThreadSafe sdf = new SimpleDateFormatThreadSafe(pattern);
        try {
            return sdf.parse(val);
        } catch (ParseException e) {
            throw new ParserException("Parse error:", e);
        }
    }

    default java.sql.Date toDateSQL(Date val) {
        if (val == null) return null;
        return new java.sql.Date(val.getTime());
    }

    default java.sql.Date toDateSQL(String val, String pattern) {
        if (val == null) return null;
        SimpleDateFormatThreadSafe sdf = new SimpleDateFormatThreadSafe(pattern);
        try {
            Date dt = sdf.parse(val);
            return new java.sql.Date(dt.getTime());
        } catch (ParseException e) {
            throw new ParserException("Parse error:", e);
        }
    }

    default java.sql.Timestamp toTimestamp(String val, String pattern) {
        if (val == null) return null;
        SimpleDateFormatThreadSafe sdf = new SimpleDateFormatThreadSafe(pattern);
        try {
            Date dt = sdf.parse(val);
            return new java.sql.Timestamp(dt.getTime());
        } catch (ParseException e) {
            throw new ParserException("Parse error:", e);
        }
    }

    default java.sql.Timestamp toTimeZone(java.sql.Timestamp timestamp, String timeZone) {
        if (timestamp == null) return null;
        if (timeZone == null) return timestamp;
        return new java.sql.Timestamp(timestamp.getTime() - TimeZone.getTimeZone(timeZone).getOffset(timestamp.getTime()));
    }

    default java.sql.Timestamp toTimeZone(java.sql.Timestamp timestamp, TimeZone timeZone) {
        if (timestamp == null) return null;
        if (timeZone == null) return timestamp;
        return new java.sql.Timestamp(timestamp.getTime() - timeZone.getOffset(timestamp.getTime()));
    }

    default Date toTimeZone(Date date, TimeZone timeZone) {
        if (date == null) return null;
        if (timeZone == null) return date;
        return new Date(date.getTime() - timeZone.getOffset(date.getTime()));
    }

    default java.sql.Timestamp toTimestamp(Date val) {
        if (val == null) return null;
        return new java.sql.Timestamp(val.getTime());
    }

    default java.sql.Timestamp toTimestamp(Date val, TimeZone timeZone) {
        if (val == null) return null;
        java.sql.Timestamp rv = toTimestamp(val);
        return toTimeZone(rv, timeZone);
    }

    default java.sql.Timestamp toCurrentTimestamp() {
        return new java.sql.Timestamp(new Date().getTime());
    }

    default java.sql.Timestamp toCurrentTimestampTimeZoneDefault() {
        return toTimeZone(new java.sql.Timestamp(new Date().getTime()), TimeZone.getDefault());
    }

    default java.sql.Date toCurrentDateSQL() {
        return new java.sql.Date(new Date().getTime());
    }

    default java.sql.Time toTime(String val, String pattern) {
        if (val == null) return null;
        SimpleDateFormatThreadSafe sdf = new SimpleDateFormatThreadSafe(pattern);
        try {
            Date dt = sdf.parse(val);
            return new java.sql.Time(dt.getTime());
        } catch (ParseException e) {
            throw new ParserException("Parse error:", e);
        }
    }

    default java.sql.Time toTime(Date val) {
        if (val == null) return null;
        return new java.sql.Time(val.getTime());
    }

    default String toSting(Date from, String pattern) {
        if (from == null) return null;
        return ParserDate.parse(from, new SimpleDateFormatThreadSafe(pattern));
    }

    default String toSting(Date from, String pattern, Locale locale) {
        if (from == null) return null;
        return ParserDate.parse(from, new SimpleDateFormatThreadSafe(pattern, locale));
    }
}
