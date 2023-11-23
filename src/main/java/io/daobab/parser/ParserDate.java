package io.daobab.parser;

import io.daobab.error.ParserException;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings("unused")
public class ParserDate {

    public ParserDate() {
    }

    public static Date toDateFromGreg(XMLGregorianCalendar from) {
        if (from == null) return null;
        return from.toGregorianCalendar().getTime();
    }

    public static XMLGregorianCalendar toGreg(Date from) {
        if (from == null) return null;
        GregorianCalendar gCalendar = new GregorianCalendar();
        gCalendar.setTime(from);

        try {
            XMLGregorianCalendar xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
            xmlCalendar.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
            return xmlCalendar;
        } catch (Exception ex) {
            throw new ParserException("Data parser from java.util.Date to javax.xml.datatype.XMLGregorianCalendar failed for value :" + from, ex);
        }
    }

    public static String parse(Date from, String pattern) {
        if (from == null) return null;
        return parse(from, new SimpleDateFormatThreadSafe(pattern));
    }
    static final Object synchro = "";

    public static String parse(Date from, SimpleDateFormatThreadSafe df) {
        if (from == null) return null;
        if (df == null) throw new ParserException("Invalid SimpleDateFormatThreadSafe");
        String result;

        synchronized (synchro) {
            result = df.format(from);
        }
        return result;
    }

    public static Long toLong(Date from) {
        if (from == null) return null;
        return from.getTime();
    }

    public static String toString(Date from, String pattern) {
        if (from == null) return null;
        return ParserDate.parse(from, new SimpleDateFormatThreadSafe(pattern));
    }

    public static String toString(Date from, String pattern, Locale locale) {
        if (from == null) return null;
        return ParserDate.parse(from, new SimpleDateFormatThreadSafe(pattern, locale));
    }

    public static String toString(LocalDateTime from, String pattern, Locale locale) {
        if (from == null) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, locale);
        return from.format(formatter);
    }

    public static String toString(LocalDateTime from, String pattern) {
        if (from == null) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return from.format(formatter);
    }


}
