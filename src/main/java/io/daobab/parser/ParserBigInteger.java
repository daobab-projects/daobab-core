package io.daobab.parser;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings("unused")
public class ParserBigInteger {

    private ParserBigInteger() {
    }

    public static BigDecimal toBigDecimal(BigInteger from) {
        return ParserNumber.toBigDecimal(from);
    }

    public static Boolean toBooleanBig(BigInteger from, Boolean inCaseOfNull) {
        return ParserNumber.toBoolean(from, inCaseOfNull);
    }

    public static boolean toBooleanSmall(BigInteger from, boolean inCaseOfNull) {
        return ParserNumber.toBooleanSmall(from, inCaseOfNull);
    }

    public static Double toDouble(BigInteger from) {
        return ParserNumber.toDouble(from);
    }

    public static Float toFloat(BigInteger from) {
        return ParserNumber.toFloat(from);
    }

    public static Integer toInteger(BigInteger from) {
        return ParserNumber.toInteger(from);
    }

    public static Short toShort(BigInteger from) {
        return ParserNumber.toShort(from);
    }

    public static Long toLong(BigInteger from) {
        return ParserNumber.toLong(from);
    }

    public static String toString(BigInteger from) {
        return ParserNumber.toString(from);
    }

}
