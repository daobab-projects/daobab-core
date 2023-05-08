package io.daobab.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JsonDateFormatter {

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm a z");

    private JsonDateFormatter() {
    }

    public static String toJSONString(Date date) {
        return dateFormat.format(date);
    }

    public static Date fromJSONString(String text) throws ParseException {
        return dateFormat.parse(text);
    }
}
