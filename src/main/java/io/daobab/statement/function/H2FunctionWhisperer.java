package io.daobab.statement.function;

import io.daobab.error.MandatoryFunctionParameter;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;
import io.daobab.query.marker.ColumnOrQuery;
import io.daobab.statement.function.base.DatePeriod;
import io.daobab.statement.function.base.FunctionKey;
import io.daobab.statement.function.dictionary.DictH2Function;
import io.daobab.statement.function.type.ColumnFunction;
import io.daobab.statement.function.type.ManyArgumentsFunction;
import io.daobab.statement.function.type.NoParamFunction;

import java.math.BigDecimal;
import java.sql.Timestamp;


@SuppressWarnings({"rawtypes", "unused"})
public interface H2FunctionWhisperer {


    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> ascii(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.ASCII, Integer.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> ascii(String columnIdentifier) {
        return new ColumnFunction<>(columnIdentifier, DictH2Function.ASCII, Integer.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> bitLength(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.BIT_LENGTH, Integer.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> bitLength(String column) {
        return new ColumnFunction<>(column, DictH2Function.BIT_LENGTH, Integer.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> concat(ColumnOrQuery<E, F, R> column1, ColumnOrQuery<?, ?, ?> column2) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(column1, DictH2Function.CONCAT, String.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, column2);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> concat(ColumnOrQuery<E, F, R> columnOrQuery, String str) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.CONCAT, String.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, str);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> concat(String str, ColumnOrQuery<E, F, R> columnOrQuery) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.CONCAT, String.class);
        rv.setKeyValue(ColumnFunction.BEFORE_COL, str);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> concatWs(String separator, ColumnOrQuery<E, F, R> column1, ColumnOrQuery<?, ?, ?> column2) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(column1, DictH2Function.CONCAT_WS, String.class);
        rv.setKeyValue(ColumnFunction.BEFORE_COL, separator);
        rv.setKeyValue(ColumnFunction.AFTER_COL, column2);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> instr(ColumnOrQuery<E, F, R> columnOrQuery, String str) {
        ColumnFunction<E, F, R, Integer> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.INSTR, Integer.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, str);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> instr(ColumnOrQuery<E, F, R> columnOrQuery, String str, Integer startInt) {
        ColumnFunction<E, F, R, Integer> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.INSTR, Integer.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, str);
        rv.setKeyValue(ColumnFunction.AFTER_COL2, startInt);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> left(ColumnOrQuery<E, F, R> columnOrQuery, Integer i) {
        ColumnFunction<E, F, R, Integer> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.INSTR, Integer.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, i);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> length(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.LENGTH, Integer.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> octetLength(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.OCTET_LENGTH, Integer.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> locate(ColumnOrQuery<E, F, R> columnOrQuery, String str) {
        ColumnFunction<E, F, R, Integer> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.LOCATE, Integer.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, str);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> locate(ColumnOrQuery<E, F, R> columnOrQuery, String str, Integer startint) {
        ColumnFunction<E, F, R, Integer> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.LOCATE, Integer.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, str);
        rv.setKeyValue(ColumnFunction.AFTER_COL2, startint);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> lower(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.LOWER, String.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> lpad(ColumnOrQuery<E, F, R> columnOrQuery, Integer i) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.LPAD, String.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, i);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> lpad(ColumnOrQuery<E, F, R> columnOrQuery, Integer i, String str) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.LPAD, String.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, i);
        rv.setKeyValue(ColumnFunction.AFTER_COL2, str);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> ltrim(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.LTRIM, String.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> position(String searchString, ColumnOrQuery<E, F, R> columnOrQuery) {
        ColumnFunction<E, F, R, Integer> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.POSITION, Integer.class);
        rv.setKeyValue(ColumnFunction.BEFORE_COL, searchString);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> repeat(ColumnOrQuery<E, F, R> columnOrQuery, Integer number) {
        ColumnFunction<E, F, R, Integer> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.REPEAT, Integer.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, number);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> replace(ColumnOrQuery<E, F, R> columnOrQuery, String searchStr, String replaceStr) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.REPLACE, String.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, searchStr);
        rv.setKeyValue(ColumnFunction.AFTER_COL2, replaceStr);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> right(ColumnOrQuery<E, F, R> columnOrQuery, Integer number) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.RIGHT, String.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, number);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> rpad(ColumnOrQuery<E, F, R> columnOrQuery, Integer number) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.RPAD, String.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, number);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> rpad(ColumnOrQuery<E, F, R> columnOrQuery, Integer number, String paddingString) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.RPAD, String.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, number);
        rv.setKeyValue(ColumnFunction.AFTER_COL2, paddingString);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> rtrim(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.RTRIM, String.class);
    }

    //TODO TRIM


    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> substring(ColumnOrQuery<E, F, R> column, int from, int to) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(column, DictH2Function.SUBSTRING);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, from);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, to);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation, C> ColumnFunction<E, F, R, C> substring(Class<C> clazz, ColumnOrQuery<E, F, R> column, int from, int to) {
        ColumnFunction<E, F, R, C> rv = new ColumnFunction<>(column, DictH2Function.SUBSTRING, clazz);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, from);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, to);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> substring(ColumnOrQuery<E, F, R> column, int from, ColumnOrQuery<?, ? extends Number, ?> to) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(column, DictH2Function.SUBSTRING, String.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, from);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, to);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> substring(ColumnOrQuery<E, F, R> column, ColumnOrQuery<?, Number, ?> from, ColumnOrQuery<?, Number, ?> to) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(column, DictH2Function.SUBSTRING, String.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, from);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, to);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> trim(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.TRIM, String.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> upper(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.UPPER, String.class);
    }


    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> abs(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.ABS, BigDecimal.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> acos(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.ACOS, BigDecimal.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> asin(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.ASIN, BigDecimal.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> atan(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.ATAN, BigDecimal.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> atan2(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.ATAN2, BigDecimal.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> atan2(ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<E, F, R> column2) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.ATAN2, BigDecimal.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> avg(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.AVG, BigDecimal.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> ceiling(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.CEILING, BigDecimal.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> cos(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.COS, BigDecimal.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> cosh(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.COSH, BigDecimal.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> cot(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.COT, BigDecimal.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Long> count(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.COUNT, Long.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> degrees(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.DEGREES, BigDecimal.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> exp(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.EXP, BigDecimal.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> floor(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.FLOOR, BigDecimal.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> greatest(ColumnOrQuery<?, F, ?>... columns) {
        if (columns == null) throw new MandatoryFunctionParameter(DictH2Function.GREATEST);
        return new ManyArgumentsFunction<>(DictH2Function.GREATEST, columns);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> least(ColumnOrQuery<?, F, ?>... columns) {
        if (columns == null) throw new MandatoryFunctionParameter(DictH2Function.LEAST);
        return new ManyArgumentsFunction<>(DictH2Function.LEAST, columns);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> ln(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.LN, BigDecimal.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> log(int baseNumeric, ColumnOrQuery<E, F, R> columnOrQuery) {
        ColumnFunction<E, F, R, BigDecimal> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.LOG, BigDecimal.class);
        rv.setKeyValue(ColumnFunction.BEFORE_COL, baseNumeric);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> log10(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.LOG10);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> max(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.MAX);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> min(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.MIN);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> oraHash(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.ORA_HASH, String.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> oraHash(ColumnOrQuery<E, F, R> columnOrQuery, Long bucketLong, Long seedLong) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.ORA_HASH, String.class);
        rv.setKeyValue(ColumnFunction.BEFORE_COL2, bucketLong);
        rv.setKeyValue(ColumnFunction.BEFORE_COL, seedLong);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> bitAnd(ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<?, ?, ?> secondColumn) {
        ColumnFunction<E, F, R, Integer> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.BITAND, Integer.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, secondColumn);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Boolean> bitGet(ColumnOrQuery<E, F, R> columnOrQuery, int number) {
        ColumnFunction<E, F, R, Boolean> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.BITGET, Boolean.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, number);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> bitNot(ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<?, ?, ?> secondColumn) {
        ColumnFunction<E, F, R, Integer> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.BITNOT, Integer.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, secondColumn);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> bitOr(ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<?, ?, ?> secondColumn) {
        ColumnFunction<E, F, R, Integer> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.BITOR, Integer.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, secondColumn);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> bitXor(ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<?, ?, ?> secondColumn) {
        ColumnFunction<E, F, R, Integer> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.BITXOR, Integer.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, secondColumn);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> lShift(ColumnOrQuery<E, F, R> columnOrQuery, int number) {
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.LSHIFT);
        rv.setKeyValue(ColumnFunction.AFTER_COL, number);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> rShift(ColumnOrQuery<E, F, R> columnOrQuery, int number) {
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.RSHIFT);
        rv.setKeyValue(ColumnFunction.AFTER_COL, number);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Long> mod(ColumnOrQuery<E, F, R> columnOrQuery, int number) {
        ColumnFunction<E, F, R, Long> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.MOD, Long.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, number);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> pi() {
        return new NoParamFunction<>(DictH2Function.PI, BigDecimal.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> power(ColumnOrQuery<E, F, R> columnOrQuery, int number) {
        ColumnFunction<E, F, R, BigDecimal> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.POWER, BigDecimal.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, number);
        return rv;
    }

    default <E extends Entity, F extends Number, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> radians(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.RADIANS, BigDecimal.class);
    }

    default <E extends Entity, R extends EntityRelation> ColumnFunction<E, BigDecimal, R, BigDecimal> rand() {
        return new NoParamFunction<>(DictH2Function.RAND, BigDecimal.class);
    }

    default <E extends Entity, R extends EntityRelation> ColumnFunction<E, BigDecimal, R, BigDecimal> rand(int number) {
        ColumnFunction<E, BigDecimal, R, BigDecimal> rv = new ColumnFunction<>(DictH2Function.RAND, BigDecimal.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, number);
        return rv;
    }

    default <E extends Entity, R extends EntityRelation> ColumnFunction<E, String, R, String> randomUuid() {
        return new NoParamFunction<>(DictH2Function.RANDOM_UUID, String.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> round(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.ROUND);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> round(ColumnOrQuery<E, F, R> columnOrQuery, int number) {
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.ROUND);
        rv.setKeyValue(ColumnFunction.AFTER_COL, number);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> sign(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.SIGN, Integer.class);
    }

    default <E extends Entity, F extends Number, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> sin(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.SIN, BigDecimal.class);
    }

    default <E extends Entity, F extends Number, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> sinh(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.SINH, BigDecimal.class);
    }

    default <E extends Entity, F extends Number, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> sqrt(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.SQRT, BigDecimal.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> tan(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.TAN, BigDecimal.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> tanh(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.TANH, BigDecimal.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, byte[]> encrypt(String algorithm, String keyBytes, ColumnOrQuery<E, F, R> columnOrQuery) {
        ColumnFunction<E, F, R, byte[]> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.ENCRYPT, byte[].class);
        rv.setMandatoryKeyValue(ColumnFunction.BEFORE_COL2, algorithm);
        rv.setMandatoryKeyValue(ColumnFunction.BEFORE_COL, keyBytes);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, byte[]> decrypt(String algorithm, String keyBytes, ColumnOrQuery<E, F, R> columnOrQuery) {
        ColumnFunction<E, F, R, byte[]> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.DECRYPT, byte[].class);
        rv.setMandatoryKeyValue(ColumnFunction.BEFORE_COL2, algorithm);
        rv.setMandatoryKeyValue(ColumnFunction.BEFORE_COL, keyBytes);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> hash(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.HASH);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> truncate(ColumnOrQuery<E, F, R> columnOrQuery, Integer decimals) {
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.TRUNCATE);
        rv.setKeyValue(ColumnFunction.AFTER_COL, decimals);
        return rv;
    }


    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Timestamp> currentDate() {
        return new NoParamFunction<>(DictH2Function.CURRENT_DATE, Timestamp.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Timestamp> currentTime() {
        return new NoParamFunction<>(DictH2Function.CURRENT_TIME, Timestamp.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Timestamp> currentTime(int precision) {
        ColumnFunction<E, F, R, Timestamp> rv = new ColumnFunction<>(DictH2Function.CURRENT_TIME, Timestamp.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, precision);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Timestamp> currentTimestamp() {
        return new NoParamFunction<>(DictH2Function.CURRENT_TIMESTAMP, Timestamp.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Timestamp> currentTimestamp(int precision) {
        ColumnFunction<E, F, R, Timestamp> rv = new ColumnFunction<>(DictH2Function.CURRENT_TIMESTAMP, Timestamp.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, precision);
        return rv;
    }

    default <C, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> currentTimestamp(int precision, Class<C> clazz) {
        ColumnFunction<E, F, R, C> rv = new ColumnFunction<>(DictH2Function.CURRENT_TIMESTAMP, clazz);
        rv.setKeyValue(ColumnFunction.AFTER_COL, precision);
        return rv;
    }

    default <E extends Entity, F extends java.util.Date, R extends EntityRelation> ColumnFunction<E, F, R, F> dateAdd(DatePeriod period, long add, ColumnOrQuery<E, F, R> columnOrQuery) {
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.DATEADD);
        rv.setKeyValue(ColumnFunction.BEFORE_COL2, period);
        rv.setKeyValue(ColumnFunction.BEFORE_COL, add);
        return rv;
    }


    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Long> dateDiff(DatePeriod period, ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<?, ?, ?> secondColumn) {
        ColumnFunction<E, F, R, Long> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.DATEDIFF, Long.class);
        rv.setKeyValue(ColumnFunction.BEFORE_COL, period);
        rv.setKeyValue(ColumnFunction.AFTER_COL, secondColumn);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> dayName(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.DAYNAME, String.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> dayOfMonth(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.DAY_OF_MONTH, Integer.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> dayOfWeek(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.DAY_OF_WEEK, Integer.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> isoDayOfWeek(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.ISO_DAY_OF_WEEK, Integer.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> dayOfYear(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.DAY_OF_YEAR, Integer.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Long> extract(DatePeriod period, Column<E, F, R> fromColumn) {
        ColumnFunction<E, F, R, Long> rv = new ColumnFunction<>(fromColumn, DictH2Function.EXTRACT, Long.class);
        rv.setKeyValue(ColumnFunction.BEFORE_COL2, period);
        rv.setKeyValue(ColumnFunction.BEFORE_COL, new FunctionKey(" from "));
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> formatDatetime(ColumnOrQuery<E, F, R> columnOrQuery, String formatString, String locale, String timeZone) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.FORMATDATETIME, String.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, formatString);
        rv.setKeyValue(ColumnFunction.AFTER_COL2, locale);
        rv.setKeyValue(ColumnFunction.AFTER_COL3, timeZone);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> hour(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.HOUR, Integer.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Timestamp> localTime(ColumnOrQuery<E, F, R> columnOrQuery, int precision) {
        ColumnFunction<E, F, R, Timestamp> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.LOCALTIME, Timestamp.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, precision);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Timestamp> localTime(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.LOCALTIME, Timestamp.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Timestamp> localTimestamp(ColumnOrQuery<E, F, R> columnOrQuery, int precision) {
        ColumnFunction<E, F, R, Timestamp> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.LOCALTIMESTAMP, Timestamp.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, precision);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Timestamp> localTimestamp(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.LOCALTIMESTAMP, Timestamp.class);
    }


    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> minute(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.MINUTE, Integer.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> month(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.MONTH, Integer.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> monthName(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.MONTHNAME, String.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> quarter(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.QUARTER, Integer.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> second(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.SECOND, Integer.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> week(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.WEEK, Integer.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> isoWeek(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.ISO_WEEK, Integer.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> year(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.YEAR, Integer.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> isoYear(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.ISO_YEAR, Integer.class);
    }


    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, String, R, String> camel(ColumnOrQuery<E, String, R> column) {
        return concat(upper(substring(column, 0, 1)), lower(substring(column, 1, length(column))));
    }

    default <C extends Number, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> sum(ColumnOrQuery<E, F, R> columnOrQuery, Class<C> clazz) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.SUM, clazz);
    }

    @SuppressWarnings("unchecked")
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> sum(ColumnOrQuery... columns) {
        if (columns == null) throw new MandatoryFunctionParameter(DictH2Function.SUM);
        return new ManyArgumentsFunction<>(DictH2Function.SUM, "+", columns);
    }

    @SuppressWarnings("unchecked")
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> sub(Column... columns) {
        if (columns == null) throw new MandatoryFunctionParameter(DictH2Function.SUB);
        return new ManyArgumentsFunction<>(DictH2Function.SUB, "-", columns);
    }

    default <C extends Number, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> count(ColumnOrQuery<E, F, R> columnOrQuery, Class<C> clazz) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.COUNT, clazz);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Long> count(E entity) {
        return new ColumnFunction<>(DictH2Function.COUNT, Long.class, entity);
    }

    default <C, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> replace(ColumnOrQuery<E, F, R> columnOrQuery, Class<C> clazz) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.REPLACE, clazz);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> right(ColumnOrQuery<E, F, R> columnOrQuery, int length) {
        ColumnFunction<E, F, R, Integer> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.RIGHT, Integer.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, length);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> left(ColumnOrQuery<E, F, R> columnOrQuery, int length) {
        ColumnFunction<E, F, R, Integer> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.LEFT, Integer.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, length);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> distinct(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictH2Function.DISTINCT);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Timestamp> interval(ColumnOrQuery<E, F, R> columnOrQuery, Number value, DatePeriod addUnit) {
        ColumnFunction<E, F, R, Timestamp> rv = new ColumnFunction<>(columnOrQuery, DictH2Function.INTERVAL, Timestamp.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, value);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, addUnit);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Timestamp> interval(Number value, DatePeriod addUnit) {
        NoParamFunction<E, F, R, Timestamp> rv = new NoParamFunction<>(DictH2Function.INTERVAL, Timestamp.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, value);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, addUnit);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Boolean> autoCommit() {
        return new NoParamFunction<>(DictH2Function.AUTOCOMMIT, Boolean.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> currentSchema() {
        return new NoParamFunction<>(DictH2Function.CURRENT_SCHEMA, String.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> currentCatalog() {
        return new NoParamFunction<>(DictH2Function.CURRENT_CATALOG, String.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> databasePath() {
        return new NoParamFunction<>(DictH2Function.DATABASE_PATH, String.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> diskSpaceUsed(Entity entity) {
        if (entity == null || entity.getEntityName() == null) {
            throw new MandatoryFunctionParameter(DictH2Function.DISK_SPACE_USED);
        }
        NoParamFunction<E, F, R, String> rv = new NoParamFunction<>(DictH2Function.DISK_SPACE_USED, String.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, entity.getEntityName());
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> memoryFree() {
        return new NoParamFunction<>(DictH2Function.MEMORY_FREE, String.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> memoryUsed() {
        return new NoParamFunction<>(DictH2Function.MEMORY_USED, String.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Boolean> readOnly() {
        return new NoParamFunction<>(DictH2Function.READONLY, Boolean.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> h2Version() {
        return new NoParamFunction<>(DictH2Function.H2VERSION, String.class);
    }

}
