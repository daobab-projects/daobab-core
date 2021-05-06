package io.daobab.parser;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public interface ParserBigDecimal {

    static BigInteger toBigInteger(BigDecimal from) {
        return ParserNumber.toBigInteger(from);
    }

    static Boolean toBooleanBig(BigDecimal from, Boolean inCaseOfNull) {
        return ParserNumber.toBoolean(from, inCaseOfNull);
    }

    static boolean toBooleanSmall(BigDecimal from, boolean inCaseOfNull) {
        return ParserNumber.toBooleanSmall(from, inCaseOfNull);
    }

    static Double toDouble(BigDecimal from) {
        return ParserNumber.toDouble(from);
    }

    static Float toFloat(BigDecimal from) {
        return ParserNumber.toFloat(from);
    }

    static Short toShort(BigDecimal from) {
        return ParserNumber.toShort(from);
    }

    static Integer toInteger(BigDecimal from) {
        return ParserNumber.toInteger(from);
    }

    static Long toLong(BigDecimal from) {
        return ParserNumber.toLong(from);
    }

    static String toString(BigDecimal from) {
        return ParserNumber.toString(from);
    }

}
