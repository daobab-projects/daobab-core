package io.daobab.parser;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings("unused")
public class ParserBigDecimal {

    private ParserBigDecimal() {
    }

    public static BigInteger toBigInteger(BigDecimal from) {
        return ParserNumber.toBigInteger(from);
    }

    public static Boolean toBooleanBig(BigDecimal from, Boolean inCaseOfNull) {
        return ParserNumber.toBoolean(from, inCaseOfNull);
    }

    public static boolean toBooleanSmall(BigDecimal from, boolean inCaseOfNull) {
        return ParserNumber.toBooleanSmall(from, inCaseOfNull);
    }

    public static Double toDouble(BigDecimal from) {
        return ParserNumber.toDouble(from);
    }

    public static Float toFloat(BigDecimal from) {
        return ParserNumber.toFloat(from);
    }

    public static Short toShort(BigDecimal from) {
        return ParserNumber.toShort(from);
    }

    public static Integer toInteger(BigDecimal from) {
        return ParserNumber.toInteger(from);
    }

    public static Long toLong(BigDecimal from) {
        return ParserNumber.toLong(from);
    }

    public static String toString(BigDecimal from) {
        return ParserNumber.toString(from);
    }

}
