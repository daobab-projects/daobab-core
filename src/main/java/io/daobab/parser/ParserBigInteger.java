package io.daobab.parser;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public interface ParserBigInteger {

    static BigDecimal toBigDecimal(BigInteger from) {
        return ParserNumber.toBigDecimal(from);
    }

    static Boolean toBooleanBig(BigInteger from, Boolean inCaseOfNull) {
        return ParserNumber.toBoolean(from, inCaseOfNull);
    }

    static boolean toBooleanSmall(BigInteger from, boolean inCaseOfNull) {
        return ParserNumber.toBooleanSmall(from, inCaseOfNull);
    }

    static Double toDouble(BigInteger from) {
        return ParserNumber.toDouble(from);
    }

    static Float toFloat(BigInteger from) {
        return ParserNumber.toFloat(from);
    }

    static Integer toInteger(BigInteger from) {
        return ParserNumber.toInteger(from);
    }

    static Short toShort(BigInteger from) {
        return ParserNumber.toShort(from);
    }

    static Long toLong(BigInteger from) {
        return ParserNumber.toLong(from);
    }

    static String toString(BigInteger from) {
        return ParserNumber.toString(from);
    }

}
