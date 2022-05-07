package io.daobab.parser;

import io.daobab.error.ParserException;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
@SuppressWarnings("unused")
public interface ParserNumber {

    static Double toDouble(Number from) {
        if (from == null) return null;
        return from.doubleValue();
    }

    static double toSmallDouble(Number from) {
        if (from == null) return 0;
        return from.doubleValue();
    }

    static Integer toInteger(Number from) {
        if (from == null) return null;
        return from.intValue();
    }

    static int toSmallInteger(Number from) {
        if (from == null) return 0;
        return from.intValue();
    }

    static Long toLong(Number from) {
        if (from == null) return null;
        return from.longValue();
    }

    static long toSmallLong(Number from) {
        if (from == null) return 0;
        return from.longValue();
    }

    static Short toShort(Number from) {
        if (from == null) return null;
        return from.shortValue();
    }

    static short toSmallShort(Number from) {
        if (from == null) return 0;
        return from.shortValue();
    }

    static Float toFloat(Number from) {
        if (from == null) return null;
        return from.floatValue();
    }

    static float toSmallFloat(Number from) {
        if (from == null) return 0;
        return from.floatValue();
    }

    static String toString(Number from) {
        if (from == null) return null;
        return from.toString();
    }

    static String toHexString(Number from) {
        if (from == null) return null;
        return String.format("%X", from);
    }

    static BigDecimal toBigDecimal(Number from) {
        if (from == null) return null;
        return new BigDecimal(from.toString());
    }


    static BigInteger toBigInteger(Number from) {
        if (from == null) return null;
        return BigInteger.valueOf(from.intValue());
    }


    static Boolean toBooleanBig(Number from) {
        if (from == null) return null;
        return toBooleanSmall(from, false);
    }

    static Boolean toBoolean(Number from, Boolean inCaseOfNull) {
        if (from == null) return inCaseOfNull;
        return toBooleanSmall(from, false);
    }

    static boolean toBooleanSmall(Number data, boolean inCaseOfNull) {
        if (data == null) return inCaseOfNull;
        if (data.intValue() == 1) {
            return true;
        } else if (data.intValue() == 0) {
            return false;
        } else {
            throw new ParserException("Value '" + data + "' can not be converted to boolean");
        }
    }


}
