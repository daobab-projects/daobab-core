package io.daobab.parser;

import io.daobab.error.ParserException;
import io.daobab.validator.ValidatorNumeric;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings("unused")
public class ParserString {

    private ParserString() {
    }

    public static BigDecimal toBigDecimal(String from) {
        if (from == null || from.trim().isEmpty()) return null;
        ValidatorNumeric.validateNumber(from);
        return new BigDecimal(from);
    }

    public static BigInteger toBigInteger(String from) {
        if (from == null || from.trim().isEmpty()) return null;
        ValidatorNumeric.validateNumber(from);
        return new BigInteger(from);
    }


    public static boolean toBooleanSmall(String from, boolean inCaseOfNull) {
        if (from == null || from.isEmpty()) return inCaseOfNull;
        String data = from.trim();
        if (data.isEmpty()) return inCaseOfNull;
        if (data.equalsIgnoreCase("true") || data.equalsIgnoreCase("false")) {
            return data.equalsIgnoreCase("true");
        } else if (data.equals("1") || data.equals("0")) {
            return data.equals("1");
        } else if (data.equalsIgnoreCase("T") || data.equalsIgnoreCase("N")) {
            return data.equalsIgnoreCase("T");
        } else if (data.equalsIgnoreCase("Y")) {
            return data.equalsIgnoreCase("Y");
        } else if (data.equalsIgnoreCase("yes") || data.equalsIgnoreCase("no")) {
            return data.equalsIgnoreCase("yes");
        } else {
            throw new ParserException("Value '" + from + "' can not be translated to boolean.");
        }
    }

    public static Boolean toBoolean(String from, String inCaseOfTrue, String inCaseOfFalse) {
        if (from == null || from.trim().equals("")) return null;
        if (from.equals(inCaseOfTrue)) return true;
        if (from.equals(inCaseOfFalse)) return false;
        throw new ParserException("Value '" + from + "' can not be translated to boolean.");
    }

    public static Boolean toBooleanBig(String from) {
        return from == null ? null : toBooleanSmall(from, false);
    }

    public static boolean toBooleanSmall(String from) {
        return toBooleanSmall(from, false);
    }


    public static Double toDouble(String from) {
        if (from == null || from.trim().isEmpty()) return null;
        return Double.parseDouble(ValidatorNumeric.validateNumber(from));
    }

    public static Float toFloat(String from) {
        if (from == null || from.trim().isEmpty()) return null;
        return Float.parseFloat(ValidatorNumeric.validateNumber(from));
    }

    public static Integer toInteger(String from) {
        if (from == null || from.trim().isEmpty()) return null;
        return Integer.parseInt(ValidatorNumeric.validateNumber(from));
    }

    public static int toSmallIntegerWithDefault(String from, int defaultValueWhenNull) {
        if (from == null || from.trim().isEmpty()) return defaultValueWhenNull;
        return Integer.parseInt(ValidatorNumeric.validateNumber(from));
    }

    public static Long toLong(String from) {
        if (from == null || from.trim().isEmpty()) return null;
        return Long.parseLong(ValidatorNumeric.validateNumber(from));
    }

    public static byte[] toByteArrayFromUTF8(String from) {
        if (from == null) return new byte[]{};
        return from.getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] toByteArray(String from, String charset) {
        if (from == null) return new byte[]{};
        try {
            return from.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new ParserException("UnsupportedEncodingException", e);
        }
    }

    public static byte[] toBytes(String from) {
        if (from == null) return new byte[]{};
        return from.getBytes(StandardCharsets.UTF_8);
    }

    public static String toShorterString(String from, int length) {
        return (from == null || from.length() <= length) ? from : from.substring(0, length);
    }

    public static List<String> splitByDot(String str) {
        if (str == null) return new ArrayList<>();
        return Arrays.asList(str.split("\\."));
    }
}
