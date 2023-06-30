package io.daobab.statement.function;

import io.daobab.error.MandatoryFunctionParameter;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.RelatedTo;
import io.daobab.query.marker.ColumnOrQuery;
import io.daobab.statement.function.base.DatePeriod;
import io.daobab.statement.function.base.FunctionKey;
import io.daobab.statement.function.dictionary.DictFunctionH2;
import io.daobab.statement.function.type.ColumnFunction;
import io.daobab.statement.function.type.ManyArgumentsFunction;
import io.daobab.statement.function.type.NoParamFunction;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings({"rawtypes", "unused"})
public interface FunctionWhispererH2 {

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Integer> ascii(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.ASCII, Integer.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Integer> ascii(String columnIdentifier) {
        return new ColumnFunction<>(columnIdentifier, DictFunctionH2.ASCII, Integer.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Integer> bitLength(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.BIT_LENGTH, Integer.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Integer> bitLength(String column) {
        return new ColumnFunction<>(column, DictFunctionH2.BIT_LENGTH, Integer.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, String> concat(ColumnOrQuery<E, F, R> column1, ColumnOrQuery<?, ?, ?> column2) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(column1, DictFunctionH2.CONCAT, String.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, column2);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, String> concat(ColumnOrQuery<E, F, R> columnOrQuery, String str) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.CONCAT, String.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, str);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, String> concat(String str, ColumnOrQuery<E, F, R> columnOrQuery) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.CONCAT, String.class);
        rv.setKeyValue(ColumnFunction.BEFORE_COL, str);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, String> concatWs(String separator, ColumnOrQuery<E, F, R> column1, ColumnOrQuery<?, ?, ?> column2) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(column1, DictFunctionH2.CONCAT_WS, String.class);
        rv.setKeyValue(ColumnFunction.BEFORE_COL, separator);
        rv.setKeyValue(ColumnFunction.AFTER_COL, column2);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Integer> instr(ColumnOrQuery<E, F, R> columnOrQuery, String str) {
        ColumnFunction<E, F, R, Integer> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.INSTR, Integer.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, str);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Integer> instr(ColumnOrQuery<E, F, R> columnOrQuery, String str, Integer startInt) {
        ColumnFunction<E, F, R, Integer> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.INSTR, Integer.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, str);
        rv.setKeyValue(ColumnFunction.AFTER_COL2, startInt);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Integer> left(ColumnOrQuery<E, F, R> columnOrQuery, Integer i) {
        ColumnFunction<E, F, R, Integer> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.INSTR, Integer.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, i);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Integer> length(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.LENGTH, Integer.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Integer> octetLength(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.OCTET_LENGTH, Integer.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Integer> locate(ColumnOrQuery<E, F, R> columnOrQuery, String str) {
        ColumnFunction<E, F, R, Integer> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.LOCATE, Integer.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, str);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Integer> locate(ColumnOrQuery<E, F, R> columnOrQuery, String str, Integer startint) {
        ColumnFunction<E, F, R, Integer> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.LOCATE, Integer.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, str);
        rv.setKeyValue(ColumnFunction.AFTER_COL2, startint);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, String> lower(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.LOWER, String.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, String> lpad(ColumnOrQuery<E, F, R> columnOrQuery, Integer i) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.LPAD, String.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, i);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, String> lpad(ColumnOrQuery<E, F, R> columnOrQuery, Integer i, String str) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.LPAD, String.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, i);
        rv.setKeyValue(ColumnFunction.AFTER_COL2, str);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, String> ltrim(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.LTRIM, String.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Integer> position(String searchString, ColumnOrQuery<E, F, R> columnOrQuery) {
        ColumnFunction<E, F, R, Integer> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.POSITION, Integer.class);
        rv.setKeyValue(ColumnFunction.BEFORE_COL, searchString);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Integer> repeat(ColumnOrQuery<E, F, R> columnOrQuery, Integer number) {
        ColumnFunction<E, F, R, Integer> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.REPEAT, Integer.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, number);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, String> replace(ColumnOrQuery<E, F, R> columnOrQuery, String searchStr, String replaceStr) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.REPLACE, String.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, searchStr);
        rv.setKeyValue(ColumnFunction.AFTER_COL2, replaceStr);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, String> right(ColumnOrQuery<E, F, R> columnOrQuery, Integer number) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.RIGHT, String.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, number);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, String> rpad(ColumnOrQuery<E, F, R> columnOrQuery, Integer number) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.RPAD, String.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, number);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, String> rpad(ColumnOrQuery<E, F, R> columnOrQuery, Integer number, String paddingString) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.RPAD, String.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, number);
        rv.setKeyValue(ColumnFunction.AFTER_COL2, paddingString);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, String> rtrim(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.RTRIM, String.class);
    }


    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, String> substring(ColumnOrQuery<E, F, R> column, int from, int to) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(column, DictFunctionH2.SUBSTRING);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, from);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, to);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo, C> ColumnFunction<E, F, R, C> substring(Class<C> clazz, ColumnOrQuery<E, F, R> column, int from, int to) {
        ColumnFunction<E, F, R, C> rv = new ColumnFunction<>(column, DictFunctionH2.SUBSTRING, clazz);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, from);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, to);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, String> substring(ColumnOrQuery<E, F, R> column, int from, ColumnOrQuery<?, ? extends Number, ?> to) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(column, DictFunctionH2.SUBSTRING, String.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, from);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, to);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, String> substring(ColumnOrQuery<E, F, R> column, ColumnOrQuery<?, Number, ?> from, ColumnOrQuery<?, Number, ?> to) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(column, DictFunctionH2.SUBSTRING, String.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, from);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, to);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, String> trim(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.TRIM, String.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, String> upper(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.UPPER, String.class);
    }


    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, BigDecimal> abs(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.ABS, BigDecimal.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, BigDecimal> acos(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.ACOS, BigDecimal.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, BigDecimal> asin(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.ASIN, BigDecimal.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, BigDecimal> atan(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.ATAN, BigDecimal.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, BigDecimal> atan2(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.ATAN2, BigDecimal.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, BigDecimal> atan2(ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<E, F, R> column2) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.ATAN2, BigDecimal.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, BigDecimal> avg(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.AVG, BigDecimal.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, BigDecimal> ceiling(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.CEILING, BigDecimal.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, BigDecimal> cos(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.COS, BigDecimal.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, BigDecimal> cosh(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.COSH, BigDecimal.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, BigDecimal> cot(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.COT, BigDecimal.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Long> count(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.COUNT, Long.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, BigDecimal> degrees(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.DEGREES, BigDecimal.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, BigDecimal> exp(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.EXP, BigDecimal.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, BigDecimal> floor(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.FLOOR, BigDecimal.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, F> greatest(ColumnOrQuery<?, F, ?>... columns) {
        if (columns == null) throw new MandatoryFunctionParameter(DictFunctionH2.GREATEST);
        return new ManyArgumentsFunction<>(DictFunctionH2.GREATEST, columns);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, F> least(ColumnOrQuery<?, F, ?>... columns) {
        if (columns == null) throw new MandatoryFunctionParameter(DictFunctionH2.LEAST);
        return new ManyArgumentsFunction<>(DictFunctionH2.LEAST, columns);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, BigDecimal> ln(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.LN, BigDecimal.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, BigDecimal> log(int baseNumeric, ColumnOrQuery<E, F, R> columnOrQuery) {
        ColumnFunction<E, F, R, BigDecimal> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.LOG, BigDecimal.class);
        rv.setKeyValue(ColumnFunction.BEFORE_COL, baseNumeric);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, F> log10(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.LOG10);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, F> max(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.MAX);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, F> min(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.MIN);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, String> oraHash(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.ORA_HASH, String.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, String> oraHash(ColumnOrQuery<E, F, R> columnOrQuery, Long bucketLong, Long seedLong) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.ORA_HASH, String.class);
        rv.setKeyValue(ColumnFunction.BEFORE_COL2, bucketLong);
        rv.setKeyValue(ColumnFunction.BEFORE_COL, seedLong);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Integer> bitAnd(ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<?, ?, ?> secondColumn) {
        ColumnFunction<E, F, R, Integer> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.BITAND, Integer.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, secondColumn);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Boolean> bitGet(ColumnOrQuery<E, F, R> columnOrQuery, int number) {
        ColumnFunction<E, F, R, Boolean> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.BITGET, Boolean.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, number);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Integer> bitNot(ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<?, ?, ?> secondColumn) {
        ColumnFunction<E, F, R, Integer> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.BITNOT, Integer.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, secondColumn);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Integer> bitOr(ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<?, ?, ?> secondColumn) {
        ColumnFunction<E, F, R, Integer> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.BITOR, Integer.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, secondColumn);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Integer> bitXor(ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<?, ?, ?> secondColumn) {
        ColumnFunction<E, F, R, Integer> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.BITXOR, Integer.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, secondColumn);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, F> lShift(ColumnOrQuery<E, F, R> columnOrQuery, int number) {
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.LSHIFT);
        rv.setKeyValue(ColumnFunction.AFTER_COL, number);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, F> rShift(ColumnOrQuery<E, F, R> columnOrQuery, int number) {
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.RSHIFT);
        rv.setKeyValue(ColumnFunction.AFTER_COL, number);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Long> mod(ColumnOrQuery<E, F, R> columnOrQuery, int number) {
        ColumnFunction<E, F, R, Long> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.MOD, Long.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, number);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, BigDecimal> pi() {
        return new NoParamFunction<>(DictFunctionH2.PI, BigDecimal.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, BigDecimal> power(ColumnOrQuery<E, F, R> columnOrQuery, int number) {
        ColumnFunction<E, F, R, BigDecimal> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.POWER, BigDecimal.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, number);
        return rv;
    }

    default <E extends Entity, F extends Number, R extends RelatedTo> ColumnFunction<E, F, R, BigDecimal> radians(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.RADIANS, BigDecimal.class);
    }

    default <E extends Entity, R extends RelatedTo> ColumnFunction<E, BigDecimal, R, BigDecimal> rand() {
        return new NoParamFunction<>(DictFunctionH2.RAND, BigDecimal.class);
    }

    default <E extends Entity, R extends RelatedTo> ColumnFunction<E, BigDecimal, R, BigDecimal> rand(int number) {
        ColumnFunction<E, BigDecimal, R, BigDecimal> rv = new ColumnFunction<>(DictFunctionH2.RAND, BigDecimal.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, number);
        return rv;
    }

    default <E extends Entity, R extends RelatedTo> ColumnFunction<E, String, R, String> randomUuid() {
        return new NoParamFunction<>(DictFunctionH2.RANDOM_UUID, String.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, F> round(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.ROUND);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, F> round(ColumnOrQuery<E, F, R> columnOrQuery, int number) {
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.ROUND);
        rv.setKeyValue(ColumnFunction.AFTER_COL, number);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Integer> sign(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.SIGN, Integer.class);
    }

    default <E extends Entity, F extends Number, R extends RelatedTo> ColumnFunction<E, F, R, BigDecimal> sin(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.SIN, BigDecimal.class);
    }

    default <E extends Entity, F extends Number, R extends RelatedTo> ColumnFunction<E, F, R, BigDecimal> sinh(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.SINH, BigDecimal.class);
    }

    default <E extends Entity, F extends Number, R extends RelatedTo> ColumnFunction<E, F, R, BigDecimal> sqrt(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.SQRT, BigDecimal.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, BigDecimal> tan(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.TAN, BigDecimal.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, BigDecimal> tanh(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.TANH, BigDecimal.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, byte[]> encrypt(String algorithm, String keyBytes, ColumnOrQuery<E, F, R> columnOrQuery) {
        ColumnFunction<E, F, R, byte[]> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.ENCRYPT, byte[].class);
        rv.setMandatoryKeyValue(ColumnFunction.BEFORE_COL2, algorithm);
        rv.setMandatoryKeyValue(ColumnFunction.BEFORE_COL, keyBytes);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, byte[]> decrypt(String algorithm, String keyBytes, ColumnOrQuery<E, F, R> columnOrQuery) {
        ColumnFunction<E, F, R, byte[]> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.DECRYPT, byte[].class);
        rv.setMandatoryKeyValue(ColumnFunction.BEFORE_COL2, algorithm);
        rv.setMandatoryKeyValue(ColumnFunction.BEFORE_COL, keyBytes);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, F> hash(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.HASH);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, F> truncate(ColumnOrQuery<E, F, R> columnOrQuery, Integer decimals) {
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.TRUNCATE);
        rv.setKeyValue(ColumnFunction.AFTER_COL, decimals);
        return rv;
    }


    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Timestamp> currentDate() {
        return new NoParamFunction<>(DictFunctionH2.CURRENT_DATE, Timestamp.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Timestamp> currentTime() {
        return new NoParamFunction<>(DictFunctionH2.CURRENT_TIME, Timestamp.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Timestamp> currentTime(int precision) {
        ColumnFunction<E, F, R, Timestamp> rv = new ColumnFunction<>(DictFunctionH2.CURRENT_TIME, Timestamp.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, precision);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Timestamp> currentTimestamp() {
        return new NoParamFunction<>(DictFunctionH2.CURRENT_TIMESTAMP, Timestamp.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Timestamp> currentTimestamp(int precision) {
        ColumnFunction<E, F, R, Timestamp> rv = new ColumnFunction<>(DictFunctionH2.CURRENT_TIMESTAMP, Timestamp.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, precision);
        return rv;
    }

    default <C, E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, C> currentTimestamp(int precision, Class<C> clazz) {
        ColumnFunction<E, F, R, C> rv = new ColumnFunction<>(DictFunctionH2.CURRENT_TIMESTAMP, clazz);
        rv.setKeyValue(ColumnFunction.AFTER_COL, precision);
        return rv;
    }

    default <E extends Entity, F extends java.util.Date, R extends RelatedTo> ColumnFunction<E, F, R, F> dateAdd(DatePeriod period, long add, ColumnOrQuery<E, F, R> columnOrQuery) {
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.DATEADD);
        rv.setKeyValue(ColumnFunction.BEFORE_COL2, period);
        rv.setKeyValue(ColumnFunction.BEFORE_COL, add);
        return rv;
    }


    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Long> dateDiff(DatePeriod period, ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<?, ?, ?> secondColumn) {
        ColumnFunction<E, F, R, Long> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.DATEDIFF, Long.class);
        rv.setKeyValue(ColumnFunction.BEFORE_COL, period);
        rv.setKeyValue(ColumnFunction.AFTER_COL, secondColumn);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, String> dayName(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.DAYNAME, String.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Integer> dayOfMonth(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.DAY_OF_MONTH, Integer.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Integer> dayOfWeek(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.DAY_OF_WEEK, Integer.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Integer> isoDayOfWeek(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.ISO_DAY_OF_WEEK, Integer.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Integer> dayOfYear(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.DAY_OF_YEAR, Integer.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Long> extract(DatePeriod period, Column<E, F, R> fromColumn) {
        ColumnFunction<E, F, R, Long> rv = new ColumnFunction<>(fromColumn, DictFunctionH2.EXTRACT, Long.class);
        rv.setKeyValue(ColumnFunction.BEFORE_COL2, period);
        rv.setKeyValue(ColumnFunction.BEFORE_COL, new FunctionKey(" from "));
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, String> formatDatetime(ColumnOrQuery<E, F, R> columnOrQuery, String formatString, String locale, String timeZone) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.FORMATDATETIME, String.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, formatString);
        rv.setKeyValue(ColumnFunction.AFTER_COL2, locale);
        rv.setKeyValue(ColumnFunction.AFTER_COL3, timeZone);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Integer> hour(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.HOUR, Integer.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Timestamp> localTime(ColumnOrQuery<E, F, R> columnOrQuery, int precision) {
        ColumnFunction<E, F, R, Timestamp> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.LOCALTIME, Timestamp.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, precision);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Timestamp> localTime(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.LOCALTIME, Timestamp.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Timestamp> localTimestamp(ColumnOrQuery<E, F, R> columnOrQuery, int precision) {
        ColumnFunction<E, F, R, Timestamp> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.LOCALTIMESTAMP, Timestamp.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, precision);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Timestamp> localTimestamp(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.LOCALTIMESTAMP, Timestamp.class);
    }


    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Integer> minute(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.MINUTE, Integer.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Integer> month(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.MONTH, Integer.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, String> monthName(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.MONTHNAME, String.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Integer> quarter(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.QUARTER, Integer.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Integer> second(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.SECOND, Integer.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Integer> week(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.WEEK, Integer.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Integer> isoWeek(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.ISO_WEEK, Integer.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Integer> year(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.YEAR, Integer.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Integer> isoYear(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.ISO_YEAR, Integer.class);
    }


    default <E extends Entity, R extends RelatedTo> ColumnFunction<E, String, R, String> camel(ColumnOrQuery<E, String, R> column) {
        return concat(upper(substring(column, 0, 1)), lower(substring(column, 2, length(column))));
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, ?> sum(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.SUM);
    }

    default <C extends Number, E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, C> sum(ColumnOrQuery<E, F, R> columnOrQuery, Class<C> clazz) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.SUM, clazz);
    }

    @SuppressWarnings("unchecked")
    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, F> sumRows(ColumnOrQuery<E, F, R> column, ColumnOrQuery<E, ?, ?>... columns) {
        if (column == null) throw new MandatoryFunctionParameter(DictFunctionH2.SUM2);
        return new ManyArgumentsFunction<>(DictFunctionH2.SUM2, "+", column, columns);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, F> sub(ColumnOrQuery column) {
        return new ManyArgumentsFunction<>(DictFunctionH2.SUB2);
    }

    @SuppressWarnings("unchecked")
    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, F> sub(ColumnOrQuery column, Column... columns) {
        if (columns == null) throw new MandatoryFunctionParameter(DictFunctionH2.SUB2);
        return new ManyArgumentsFunction<>(DictFunctionH2.SUB2, "-", columns);
    }

    default <C extends Number, E extends Entity & RelatedTo> ColumnFunction<E, Long, E, C> count(ColumnOrQuery<E, Long, E> columnOrQuery, Class<C> clazz) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.COUNT, clazz);
    }

    default <E extends Entity & RelatedTo> ColumnFunction<E, Long, E, Long> count(E entity) {
        return new ColumnFunction<>(DictFunctionH2.COUNT, Long.class, entity);
    }

    default <C, E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, C> replace(ColumnOrQuery<E, F, R> columnOrQuery, Class<C> clazz) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.REPLACE, clazz);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Integer> right(ColumnOrQuery<E, F, R> columnOrQuery, int length) {
        ColumnFunction<E, F, R, Integer> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.RIGHT, Integer.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, length);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Integer> left(ColumnOrQuery<E, F, R> columnOrQuery, int length) {
        ColumnFunction<E, F, R, Integer> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.LEFT, Integer.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, length);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, F> distinct(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionH2.DISTINCT);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Timestamp> interval(ColumnOrQuery<E, F, R> columnOrQuery, Number value, DatePeriod addUnit) {
        ColumnFunction<E, F, R, Timestamp> rv = new ColumnFunction<>(columnOrQuery, DictFunctionH2.INTERVAL, Timestamp.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, value);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, addUnit);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Timestamp> interval(Number value, DatePeriod addUnit) {
        NoParamFunction<E, F, R, Timestamp> rv = new NoParamFunction<>(DictFunctionH2.INTERVAL, Timestamp.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, value);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, addUnit);
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Boolean> autoCommit() {
        return new NoParamFunction<>(DictFunctionH2.AUTOCOMMIT, Boolean.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, String> currentSchema() {
        return new NoParamFunction<>(DictFunctionH2.CURRENT_SCHEMA, String.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, String> currentCatalog() {
        return new NoParamFunction<>(DictFunctionH2.CURRENT_CATALOG, String.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, String> databasePath() {
        return new NoParamFunction<>(DictFunctionH2.DATABASE_PATH, String.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, String> diskSpaceUsed(Entity entity) {
        if (entity == null || entity.getEntityName() == null) {
            throw new MandatoryFunctionParameter(DictFunctionH2.DISK_SPACE_USED);
        }
        NoParamFunction<E, F, R, String> rv = new NoParamFunction<>(DictFunctionH2.DISK_SPACE_USED, String.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, entity.getEntityName());
        return rv;
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, String> memoryFree() {
        return new NoParamFunction<>(DictFunctionH2.MEMORY_FREE, String.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, String> memoryUsed() {
        return new NoParamFunction<>(DictFunctionH2.MEMORY_USED, String.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Boolean> readOnly() {
        return new NoParamFunction<>(DictFunctionH2.READONLY, Boolean.class);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, String> h2Version() {
        return new NoParamFunction<>(DictFunctionH2.H2VERSION, String.class);
    }

}
