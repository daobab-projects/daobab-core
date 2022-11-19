package io.daobab.statement.function;

import io.daobab.error.MandatoryFunctionParameter;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;
import io.daobab.query.marker.ColumnOrQuery;
import io.daobab.statement.function.base.DatePeriod;
import io.daobab.statement.function.dictionary.DictFunctionMicrosoftSql;
import io.daobab.statement.function.type.ColumnFunction;
import io.daobab.statement.function.type.ManyArgumentsFunction;
import io.daobab.statement.function.type.NoParamFunction;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@SuppressWarnings({"rawtypes", "unused"})
public interface FunctionWhispererMicrosoftSql {


    /**
     * Returns the ASCII value for the specific character
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> ascii(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.ASCII, Integer.class);
    }

    /**
     * Adds two or more expressions together
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> concat(ColumnOrQuery<?, F, ?>... columns) {
        if (columns == null) throw new MandatoryFunctionParameter(DictFunctionMicrosoftSql.CONCAT);
        return new ManyArgumentsFunction<>(DictFunctionMicrosoftSql.CONCAT, columns);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> concat(ColumnOrQuery<E, F, R> columnOrQuery, String str) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.CONCAT, String.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, str);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> concat(String str, ColumnOrQuery<E, F, R> columnOrQuery) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.CONCAT, String.class);
        rv.setKeyValue(ColumnFunction.BEFORE_COL, str);
        return rv;
    }

    /**
     * Adds two or more expressions together with a separator
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> concatWs(String separator, ColumnOrQuery... columns) {
        if (columns == null) throw new MandatoryFunctionParameter(DictFunctionMicrosoftSql.CONCAT_WS);
        return new ManyArgumentsFunction<>(DictFunctionMicrosoftSql.CONCAT_WS, separator, columns);
    }

    /**
     * Formats a number to a format like "#,###,###.##", rounded to a specified number of decimal places
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> format(ColumnOrQuery<E, F, R> columnOrQuery, int deciamalPlaces) {
        if (columnOrQuery == null) throw new MandatoryFunctionParameter(DictFunctionMicrosoftSql.FORMAT);
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.FORMAT);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, deciamalPlaces);
        return rv;
    }


    /**
     * Extracts a number of characters from a string (starting from left)
     *
     * @param number
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> left(ColumnOrQuery<E, F, R> columnOrQuery, int number) {
        if (columnOrQuery == null) throw new MandatoryFunctionParameter(DictFunctionMicrosoftSql.LEFT);
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.LEFT);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, number);
        return rv;
    }

    /**
     * Converts a string to lower-case
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> lower(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.LOWER, String.class);
    }

    /**
     * Removes leading spaces from a string
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> ltrim(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.LTRIM);
    }

    /**
     * Replaces all occurrences of a substring within a string, with a new substring
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> replace(ColumnOrQuery<E, F, R> columnOrQuery, String fromString, String newString) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.REPLACE);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, fromString);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, newString);
        return rv;
    }

    /**
     * Reverses a string and returns the result
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> reverse(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.REVERSE, String.class);
    }

    /**
     * Extracts a number of characters from a string (starting from right)
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> right(ColumnOrQuery<E, F, R> columnOrQuery, Integer length, String rpadString) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.RIGHT);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, length);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, rpadString);
        return rv;
    }

    /**
     * Removes trailing spaces from a string
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> rtrim(String value) {
        return new ColumnFunction<>(DictFunctionMicrosoftSql.RTRIM, String.class, value);
    }

    /**
     * Extracts a substring from a string (starting at any position)
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> substring(ColumnOrQuery<E, F, R> columnOrQuery, Number from, Number to) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.SUBSTRING);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, from);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, to);
        return rv;
    }

    /**
     * Extracts a substring from a string (starting at any position)
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> substring(ColumnOrQuery<E, F, R> columnOrQuery, int from, Column<?, ? extends Number, ?> to) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.SUBSTRING);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, from);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, to);
        return rv;
    }

    /**
     * Extracts a substring from a string (starting at any position)
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> substring(ColumnOrQuery<E, F, R> columnOrQuery, Column<?, Number, ?> from, Column<?, Number, ?> to) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.SUBSTRING);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, from);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, to);
        return rv;
    }

    /**
     * Removes leading and trailing spaces from a string
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> trim(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.TRIM, String.class);
    }

    /**
     * Converts a string to upper-case
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> upper(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.UPPER, String.class);
    }


    /**
     * The ABS() function returns the absolute (positive) value of a number.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> abs(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.ABS);
    }

    /**
     * The ACOS() function returns the arc cosine of a number.
     * The specified number must be between -1 to 1, otherwise this function returns NULL.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> acos(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.ACOS, BigDecimal.class);
    }

    /**
     * The ASIN() function returns the arc sine of a number.
     * <p>
     * The specified number must be between -1 to 1, otherwise this function returns NULL.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> asin(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.ASIN, BigDecimal.class);
    }

    /**
     * The ATAN() function returns the arc tangent of one or two numbers.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> atan(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.ATAN, BigDecimal.class);
    }

    /**
     *
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> atan(ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<E, F, R> columnOrQuery2) {
        ColumnFunction<E, F, R, BigDecimal> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.ATAN, BigDecimal.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, columnOrQuery2);
        return rv;
    }

    /**
     * The ATAN() function returns the arc tangent of one or two numbers.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> atan2(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.ATAN2, BigDecimal.class);
    }

    /**
     * The ATAN2() function returns the arc tangent of two numbers.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> atan2(ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<E, F, R> columnOrQuery2) {
        ColumnFunction<E, F, R, BigDecimal> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.ATAN2, BigDecimal.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, columnOrQuery2);
        return rv;
    }

    /**
     * The AVG() function returns the average value of an expression.
     * <p>
     * Note: NULL values are ignored.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> avg(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.AVG, BigDecimal.class);
    }

    /**
     * The CEILING() function returns the smallest integer value that is bigger than or equal to a number.
     * <p>
     * Note: This function is equal to the CEIL() function.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> ceiling(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.CEILING, Integer.class);
    }

    /**
     * The COS() function returns the cosine of a number.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> cos(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.COS, BigDecimal.class);
    }

    /**
     * The COT() function returns the cotangent of a number.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> cot(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.COT, BigDecimal.class);
    }

    //    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> count(ColumnOrQuery<E, F, R> columnOrQuery) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.COUNT);
//    }

    /**
     * The DEGREES() function converts a value in radians to degrees.
     * <p>
     * Note: See also the RADIANS() and PI() functions.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> degrees(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.DEGREES, BigDecimal.class);
    }

    /**
     * The EXP() function returns e raised to the power of the specified number.
     * <p>
     * The constant e (2.718281...), is the base of natural logarithms.
     * <p>
     * Tip: Also look at the LOG() and LN() functions.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> exp(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.EXP, BigDecimal.class);
    }

    /**
     * The LOG() function returns the natural logarithm of a specified number, or the logarithm of the number to the specified base.
     * <p>
     * Note: See also the LN() and EXP() functions.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> log(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.LOG, BigDecimal.class);
    }

    /**
     * The LOG10() function returns the natural logarithm of a number to base-10.
     * <p>
     * Note: See also the LOG() function.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> log10(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.LOG10, BigDecimal.class);
    }

    /**
     * The MAX() function returns the maximum value in a set of values.
     * <p>
     * Note: See also the MIN() function.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> max(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.MAX);
    }

    /**
     * The MIN() function returns the minimum value in a set of values.
     * <p>
     * Note: See also the MAX() function.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> min(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.MIN);
    }

    /**
     * The PI() function returns the value of PI.
     * <p>
     * Note: See also the DEGREES() and RADIANS() functions.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> pi() {
        return new NoParamFunction<>(DictFunctionMicrosoftSql.PI, BigDecimal.class);
    }

    /**
     * The POWER() function returns the value of a number raised to the power of another number.
     * <p>
     * Note: This function is equal to the POW() function.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> power(ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<E, F, R> columnOrQuery2) {
        ColumnFunction<E, F, R, BigDecimal> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.POWER);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, columnOrQuery2);
        return rv;
    }

    /**
     * The RADIANS() function converts a degree value into radians.
     * <p>
     * Note: See also the DEGREES() and PI() functions.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> radians(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.RADIANS, BigDecimal.class);
    }

    /**
     * The RAND() function returns a random number between 0 (inclusive) and 1 (exclusive).
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> rand(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.RAND, BigDecimal.class);
    }

    /**
     * The ROUND() function rounds a number to a specified number of decimal places.
     * <p>
     * Note: See also the FLOOR(), CEIL(), CEILING(), and TRUNCATE() functions.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> round(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.ROUND);
    }

    /**
     * The ROUND() function rounds a number to a specified number of decimal places.
     * <p>
     * Note: See also the FLOOR(), CEIL(), CEILING(), and TRUNCATE() functions.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> round(ColumnOrQuery<E, F, R> columnOrQuery, Integer decimals) {
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.ROUND);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, decimals);
        return rv;
    }

    /**
     * The SIGN() function returns the sign of a number.
     * <p>
     * This function will return one of the following:
     * <p>
     * If number > 0, it returns 1
     * If number = 0, it returns 0
     * If number < 0, it returns -1
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> sign(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.SIGN, Integer.class);
    }

    /**
     * The SIN() function returns the sine of a number.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> sin(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.SIN, BigDecimal.class);
    }

    /**
     * The SQRT() function returns the square root of a number.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> sqrt(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.SQRT, BigDecimal.class);
    }

    /**
     * The SUM() function calculates the sum of a set of values.
     * <p>
     * Note: NULL values are ignored.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> sum(ColumnOrQuery<?, F, ?>... columns) {
        return new ManyArgumentsFunction<>(DictFunctionMicrosoftSql.SUM, BigDecimal.class, "+", columns);
    }

    /**
     * The TAN() function returns the tangent of a number.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> tan(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.TAN, BigDecimal.class);
    }

    /**
     * The CURRENT_TIMESTAMP() function returns the current date and time.
     * <p>
     * Note: The date and time is returned as "YYYY-MM-DD HH-MM-SS" (string) or as YYYYMMDDHHMMSS.uuuuuu (numeric).
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Timestamp> currentTimestamp() {
        return new NoParamFunction<>(DictFunctionMicrosoftSql.CURRENT_TIMESTAMP, Timestamp.class);
    }

    /**
     * The CURRENT_TIMESTAMP() function returns the current date and time.
     * <p>
     * Note: The date and time is returned as "YYYY-MM-DD HH-MM-SS" (string) or as YYYYMMDDHHMMSS.uuuuuu (numeric).
     */
    default <C extends Date, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> currentTimestamp(Class<C> dateClass) {
        return new NoParamFunction<>(DictFunctionMicrosoftSql.CURRENT_TIMESTAMP, dateClass);
    }

    /**
     * The DATEDIFF() function returns the number of days between two date values.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Long> dateDiff(ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<E, F, R> columnOrQuery2) {
        ColumnFunction<E, F, R, Long> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.DATEDIFF, Long.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, columnOrQuery2);
        return rv;
    }

    /**
     * The DATE_ADD() function adds a time/date interval to a date and then returns the date.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> dateAdd(ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<?, ?, ?> interval) {
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.DATE_ADD);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, interval);
        return rv;
    }

    /**
     * The DAY() function returns the day of the month for a given date (a number from 1 to 31).
     * <p>
     * Note: This function equals the DAYOFMONTH() function.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> day(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.DAY, Integer.class);
    }

    /**
     * The MONTH() function returns the month part for a given date (a number from 1 to 12).
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> month(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.MONTH, Integer.class);
    }

    /**
     * The YEAR() function returns the year part for a given date (a number from 1000 to 9999).
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> year(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.YEAR, Integer.class);
    }
//
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, String, R, String> camel(Column<E, String, R> column) {
//        return concat(upper(substring(column, 0, 1)), lower(substring(column, 1, length(column))));
//    }

    default <C extends Number, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> sum(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.SUM);
    }

    default <C extends Number, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> sum(Class<C> clazz, Column<?, ?, ?>... columns) {
        return new ColumnFunction<>(DictFunctionMicrosoftSql.SUM, clazz, "+", columns);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Long> count(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.COUNT);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Long> count(E entity) {
        return new ColumnFunction<>(DictFunctionMicrosoftSql.COUNT, Long.class, entity);
    }

    default <C, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> replace(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.REPLACE);
    }

    //TODO: see substring
    default <C, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> right(ColumnOrQuery<E, F, R> columnOrQuery, int length) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.RIGHT);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> distinct(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.DISTINCT);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Timestamp> interval(ColumnOrQuery<E, F, R> columnOrQuery, Number value, DatePeriod addUnit) {
        ColumnFunction<E, F, R, Timestamp> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMicrosoftSql.INTERVAL, Timestamp.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, value);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, addUnit);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Timestamp> interval(Number value, DatePeriod addUnit) {
        NoParamFunction<E, F, R, Timestamp> rv = new NoParamFunction<>(DictFunctionMicrosoftSql.INTERVAL, Timestamp.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, value);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, addUnit);
        return rv;
    }

}
