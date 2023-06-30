package io.daobab.statement.function;

import io.daobab.model.Entity;
import io.daobab.model.RelatedTo;
import io.daobab.query.marker.ColumnOrQuery;
import io.daobab.statement.function.type.ColumnFunction;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings({"rawtypes", "unused"})
public interface FunctionWhispererBuffer {

    //
//    /**
//     * Returns the ASCII value for the specific character
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> ascii(ColumnOrQuery<E, F, R> columnOrQuery) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.ASCII, Integer.class);
//    }
//
//    /**
//     * Returns the length of a string (in characters)
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> charLength(ColumnOrQuery<E, F, R> columnOrQuery) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.CHAR_LENGTH, Integer.class);
//    }
//
//    /**
//     * Returns the length of a string (in characters)
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> characterLength(ColumnOrQuery<E, F, R> columnOrQuery) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.CHARACTER_LENGTH, Integer.class);
//    }
//
//    /**
//     * Adds two or more expressions together
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> concat(ColumnOrQuery<?, F, ?>... columns) {
//        if (columns == null) throw new MandatoryFunctionParameter(DictFunctionPostgreSql.CONCAT);
//        return new ManyArgumentsFunction<>(DictFunctionPostgreSql.CONCAT, columns);
//    }
//
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> concat(ColumnOrQuery<E, F, R> columnOrQuery, String str) {
//        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.CONCAT, String.class);
//        rv.setKeyValue(ColumnFunction.AFTER_COL, str);
//        return rv;
//    }
//
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> concat(String str, ColumnOrQuery<E, F, R> columnOrQuery) {
//        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.CONCAT, String.class);
//        rv.setKeyValue(ColumnFunction.BEFORE_COL, str);
//        return rv;
//    }
//
//    /**
//     * Adds two or more expressions together with a separator
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> concatWs(String separator, ColumnOrQuery... columns) {
//        if (columns == null) throw new MandatoryFunctionParameter(DictFunctionPostgreSql.CONCAT_WS);
//        return new ManyArgumentsFunction<>(DictFunctionPostgreSql.CONCAT_WS, separator, columns);
//    }
//
//
//    /**
//     * Converts a string to lower-case
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> lcase(ColumnOrQuery<E, F, R> columnOrQuery) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.LCASE, String.class);
//    }
//
//    /**
//     * Extracts a number of characters from a string (starting from left)
//     *
//     * @param number
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> left(ColumnOrQuery<E, F, R> columnOrQuery, int number) {
//        if (columnOrQuery == null) throw new MandatoryFunctionParameter(DictFunctionPostgreSql.LEFT);
//        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.LEFT);
//        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, number);
//        return rv;
//    }
//
//    /**
//     * Returns the length of a string (in bytes)
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> length(ColumnOrQuery<E, F, R> columnOrQuery) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.LENGTH, Integer.class);
//    }
//
//    /**
//     * Converts a string to lower-case
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> lower(ColumnOrQuery<E, F, R> columnOrQuery) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.LOWER, String.class);
//    }
//
//    /**
//     * Removes leading spaces from a string
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> ltrim(ColumnOrQuery<E, F, R> columnOrQuery) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.LTRIM);
//    }
//
//    /**
//     * Extracts a substring from a string (starting at any position)
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> mid(ColumnOrQuery<E, F, R> columnOrQuery, int start, int length) {
//        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.MID, String.class);
//        rv.setKeyValue(ColumnFunction.AFTER_COL, start);
//        rv.setKeyValue(ColumnFunction.AFTER_COL2, length);
//        return rv;
//    }
//
//    /**
//     * Returns the position of the first occurrence of a substring in a string
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> position(String substring, ColumnOrQuery<E, F, R> columnOrQuery) {
//        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.POSITION);
//        rv.setMandatoryKeyValue(ColumnFunction.BEFORE_COL2, substring);
//        rv.setKeyValue(ColumnFunction.BEFORE_COL, new FunctionKey("IN"));
//        return rv;
//    }
//
//    /**
//     * Repeats a string as many times as specified
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> repeat(ColumnOrQuery<E, F, R> columnOrQuery, Integer number) {
//        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.REPEAT);
//        rv.setMandatoryKeyValue(ColumnFunction.BEFORE_COL, number);
//        return rv;
//    }
//
//    /**
//     * Replaces all occurrences of a substring within a string, with a new substring
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> replace(ColumnOrQuery<E, F, R> columnOrQuery, String fromString, String newString) {
//        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.REPLACE);
//        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, fromString);
//        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, newString);
//        return rv;
//    }
//
//    /**
//     * Reverses a string and returns the result
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> reverse(ColumnOrQuery<E, F, R> columnOrQuery) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.REVERSE, String.class);
//    }
//
//    /**
//     * Extracts a number of characters from a string (starting from right)
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> right(ColumnOrQuery<E, F, R> columnOrQuery, Integer length, String rpadString) {
//        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.RIGHT);
//        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, length);
//        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, rpadString);
//        return rv;
//    }
//
//    /**
//     * Right-pads a string with another string, to a certain length
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> rpad(ColumnOrQuery<E, F, R> columnOrQuery, int length, String rpadString) {
//        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.RPAD);
//        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, length);
//        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, rpadString);
//        return rv;
//    }
//
//    /**
//     * Removes trailing spaces from a string
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> rtrim(String value) {
//        return new ColumnFunction<>(DictFunctionPostgreSql.RTRIM, String.class, value);
//    }
//
//    /**
//     * Right-pads a string with another string, to a certain length
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> strcmp(ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<E, F, R> columnOrQuery2) {
//        ColumnFunction<E, F, R, Integer> rv = new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.RPAD);
//        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, columnOrQuery2);
//        return rv;
//    }
//
//    /**
//     * Extracts a substring from a string (starting at any position)
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> substr(ColumnOrQuery<E, F, R> columnOrQuery, int start, int length) {
//        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.SUBSTR);
//        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, start);
//        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, length);
//        return rv;
//    }
//
//    /**
//     * Extracts a substring from a string (starting at any position)
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> substring(ColumnOrQuery<E, F, R> columnOrQuery, Number from, Number to) {
//        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.SUBSTRING);
//        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, from);
//        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, to);
//        return rv;
//    }
//
//    /**
//     * Extracts a substring from a string (starting at any position)
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> substring(ColumnOrQuery<E, F, R> columnOrQuery, int from, Column<?, ? extends Number, ?> to) {
//        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.SUBSTRING);
//        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, from);
//        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, to);
//        return rv;
//    }
//
//    /**
//     * Extracts a substring from a string (starting at any position)
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> substring(ColumnOrQuery<E, F, R> columnOrQuery, Column<?, Number, ?> from, Column<?, Number, ?> to) {
//        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.SUBSTRING);
//        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, from);
//        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, to);
//        return rv;
//    }
//
//    /**
//     * Removes leading and trailing spaces from a string
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> trim(ColumnOrQuery<E, F, R> columnOrQuery) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.TRIM, String.class);
//    }
//
//    /**
//     * Converts a string to upper-case
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> upper(ColumnOrQuery<E, F, R> columnOrQuery) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.UPPER, String.class);
//    }
//
//
//    /**
//     * The ABS() function returns the absolute (positive) value of a number.
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> abs(ColumnOrQuery<E, F, R> columnOrQuery) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.ABS);
//    }
//
//    /**
//     * The ACOS() function returns the arc cosine of a number.
//     * The specified number must be between -1 to 1, otherwise this function returns NULL.
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> acos(ColumnOrQuery<E, F, R> columnOrQuery) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.ACOS, BigDecimal.class);
//    }
//
//    /**
//     * The ASIN() function returns the arc sine of a number.
//     * <p>
//     * The specified number must be between -1 to 1, otherwise this function returns NULL.
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> asin(ColumnOrQuery<E, F, R> columnOrQuery) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.ASIN, BigDecimal.class);
//    }
//
//    /**
//     * The ATAN() function returns the arc tangent of one or two numbers.
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> atan(ColumnOrQuery<E, F, R> columnOrQuery) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.ATAN, BigDecimal.class);
//    }
//
//    /**
//     *
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> atan(ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<E, F, R> columnOrQuery2) {
//        ColumnFunction<E, F, R, BigDecimal> rv = new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.ATAN, BigDecimal.class);
//        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, columnOrQuery2);
//        return rv;
//    }
//
//    /**
//     * The ATAN() function returns the arc tangent of one or two numbers.
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> atan2(ColumnOrQuery<E, F, R> columnOrQuery) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.ATAN2, BigDecimal.class);
//    }
//
//    /**
//     * The ATAN2() function returns the arc tangent of two numbers.
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> atan2(ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<E, F, R> columnOrQuery2) {
//        ColumnFunction<E, F, R, BigDecimal> rv = new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.ATAN2, BigDecimal.class);
//        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, columnOrQuery2);
//        return rv;
//    }
//
//    /**
//     * The AVG() function returns the average value of an expression.
//     * <p>
//     * Note: NULL values are ignored.
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> avg(ColumnOrQuery<E, F, R> columnOrQuery) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.AVG, BigDecimal.class);
//    }
//
//    /**
//     * The CEIL() function returns the smallest integer value that is bigger than or equal to a number.
//     * <p>
//     * Note: This function is equal to the CEILING() function.
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> ceil(ColumnOrQuery<E, F, R> columnOrQuery) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.CEIL, Integer.class);
//    }
//
//    /**
//     * The CEILING() function returns the smallest integer value that is bigger than or equal to a number.
//     * <p>
//     * Note: This function is equal to the CEIL() function.
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> ceiling(ColumnOrQuery<E, F, R> columnOrQuery) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.CEILING, Integer.class);
//    }
//
//    /**
//     * The COS() function returns the cosine of a number.
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> cos(ColumnOrQuery<E, F, R> columnOrQuery) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.COS, BigDecimal.class);
//    }
//
//    /**
//     * The COT() function returns the cotangent of a number.
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> cot(ColumnOrQuery<E, F, R> columnOrQuery) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.COT, BigDecimal.class);
//    }
//
//    //    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> count(ColumnOrQuery<E, F, R> columnOrQuery) {
////        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.COUNT);
////    }
//
//    /**
//     * The DEGREES() function converts a value in radians to degrees.
//     * <p>
//     * Note: See also the RADIANS() and PI() functions.
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> degrees(ColumnOrQuery<E, F, R> columnOrQuery) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.DEGREES, BigDecimal.class);
//    }
//
//    /**
//     * The EXP() function returns e raised to the power of the specified number.
//     * <p>
//     * The constant e (2.718281...), is the base of natural logarithms.
//     * <p>
//     * Tip: Also look at the LOG() and LN() functions.
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> exp(ColumnOrQuery<E, F, R> columnOrQuery) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.EXP, BigDecimal.class);
//    }
//
//    /**
//     * The FLOOR() function returns the largest integer value that is smaller than or equal to a number.
//     * <p>
//     * Note: Also look at the ROUND(), CEIL(), CEILING(), TRUNCATE(), and DIV functions.
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> floor(ColumnOrQuery<E, F, R> columnOrQuery) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.FLOOR);
//    }
//
//    /**
//     * The GREATEST() function returns the greatest value of the list of arguments.
//     * <p>
//     * Note: See also the LEAST() function.
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> greatest(ColumnOrQuery<?, ?, ?>... columns) {
//        return new ManyArgumentsFunction<>(DictFunctionPostgreSql.GREATEST, columns);
//    }
//
//    /**
//     * The LEAST() function returns the smallest value of the list of arguments.
//     * <p>
//     * Note: See also the GREATEST() function.
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> least(ColumnOrQuery<?, F, ?>... columns) {
//        return new ManyArgumentsFunction<>(DictFunctionPostgreSql.LEAST, columns);
//    }
//
//
//    /**
//     * The LOG() function returns the natural logarithm of a specified number, or the logarithm of the number to the specified base.
//     * <p>
//     * Note: See also the LN() and EXP() functions.
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> log(ColumnOrQuery<E, F, R> columnOrQuery) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.LOG, BigDecimal.class);
//    }
//
//
//    /**
//     * The MAX() function returns the maximum value in a set of values.
//     * <p>
//     * Note: See also the MIN() function.
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> max(ColumnOrQuery<E, F, R> columnOrQuery) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.MAX);
//    }
//
//    /**
//     * The MIN() function returns the minimum value in a set of values.
//     * <p>
//     * Note: See also the MAX() function.
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> min(ColumnOrQuery<E, F, R> columnOrQuery) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.MIN);
//    }
//
//    /**
//     * The MOD() function returns the remainder of a number divided by another number.
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> mod(ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<E, F, R> columnOrQuery2) {
//        ColumnFunction<E, F, R, BigDecimal> rv = new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.MOD);
//        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, new FunctionKey(" MOD "));
//        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, columnOrQuery2);
//        return rv;
//    }
//
//    /**
//     * The MOD() function returns the remainder of a number divided by another number.
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> mod(ColumnOrQuery<E, F, R> columnOrQuery, Number number) {
//        ColumnFunction<E, F, R, BigDecimal> rv = new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.MOD);
//        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, new FunctionKey(" MOD "));
//        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, number);
//        return rv;
//    }
//
//    /**
//     * The MOD() function returns the remainder of a number divided by another number.
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> mod(Number number, ColumnOrQuery<E, F, R> columnOrQuery2) {
//        ColumnFunction<E, F, R, BigDecimal> rv = new ColumnFunction<>(columnOrQuery2, DictFunctionPostgreSql.MOD);
//        rv.setMandatoryKeyValue(ColumnFunction.BEFORE_COL, new FunctionKey(" MOD "));
//        rv.setMandatoryKeyValue(ColumnFunction.BEFORE_COL2, columnOrQuery2);
//        return rv;
//    }
//
//    /**
//     * The PI() function returns the value of PI.
//     * <p>
//     * Note: See also the DEGREES() and RADIANS() functions.
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> pi() {
//        return new NoParamFunction<>(DictFunctionPostgreSql.PI, BigDecimal.class);
//    }
//
//    /**
//     * The POW() function returns the value of a number raised to the power of another number.
//     * <p>
//     * Note: This function is equal to the POWER() function.
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> pow(ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<E, F, R> columnOrQuery2) {
//        ColumnFunction<E, F, R, BigDecimal> rv = new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.POW);
//        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, columnOrQuery2);
//        return rv;
//    }
//
//    /**
//     * The POWER() function returns the value of a number raised to the power of another number.
//     * <p>
//     * Note: This function is equal to the POW() function.
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> power(ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<E, F, R> columnOrQuery2) {
//        ColumnFunction<E, F, R, BigDecimal> rv = new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.POWER);
//        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, columnOrQuery2);
//        return rv;
//    }
//
//    /**
//     * The RADIANS() function converts a degree value into radians.
//     * <p>
//     * Note: See also the DEGREES() and PI() functions.
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> radians(ColumnOrQuery<E, F, R> columnOrQuery) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.RADIANS, BigDecimal.class);
//    }
//
//    /**
//     * The ROUND() function rounds a number to a specified number of decimal places.
//     * <p>
//     * Note: See also the FLOOR(), CEIL(), CEILING(), and TRUNCATE() functions.
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> round(ColumnOrQuery<E, F, R> columnOrQuery) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.ROUND);
//    }
//
//    /**
//     * The ROUND() function rounds a number to a specified number of decimal places.
//     * <p>
//     * Note: See also the FLOOR(), CEIL(), CEILING(), and TRUNCATE() functions.
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> round(ColumnOrQuery<E, F, R> columnOrQuery, Integer decimals) {
//        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.ROUND);
//        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, decimals);
//        return rv;
//    }
//
//
//    /**
//     * The SIN() function returns the sine of a number.
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> sin(ColumnOrQuery<E, F, R> columnOrQuery) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.SIN, BigDecimal.class);
//    }
//
//    /**
//     * The SQRT() function returns the square root of a number.
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> sqrt(ColumnOrQuery<E, F, R> columnOrQuery) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.SQRT, BigDecimal.class);
//    }
//
//    /**
//     * The SUM() function calculates the sum of a set of values.
//     * <p>
//     * Note: NULL values are ignored.
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> sum(ColumnOrQuery<?, F, ?>... columns) {
//        return new ManyArgumentsFunction<>(DictFunctionPostgreSql.SUM, BigDecimal.class, "+", columns);
//    }
//
//    /**
//     * The TAN() function returns the tangent of a number.
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> tan(ColumnOrQuery<E, F, R> columnOrQuery) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.TAN, BigDecimal.class);
//    }
//
//
//    /**
//     * The CURRENT_DATE() function returns the current date.
//     * <p>
//     * Note: The date is returned as "YYYY-MM-DD" (string) or as YYYYMMDD (numeric).
//     * <p>
//     * Note: This function equals the CURDATE() function.
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Date> curretDate() {
//        return new NoParamFunction<>(DictFunctionPostgreSql.CURRENT_DATE, Date.class);
//    }
//
//    /**
//     * The CURRENT_DATE() function returns the current date.
//     * <p>
//     * Note: The date is returned as "YYYY-MM-DD" (string) or as YYYYMMDD (numeric).
//     * <p>
//     * Note: This function equals the CURDATE() function.
//     */
//    default <C extends Date, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> curretDate(Class<C> dateClass) {
//        return new NoParamFunction<>(DictFunctionPostgreSql.CURRENT_DATE, dateClass);
//    }
//
//    /**
//     * The CURRENT_TIME() function returns the current time.
//     * <p>
//     * Note: The time is returned as "HH-MM-SS" (string) or as HHMMSS.uuuuuu (numeric).
//     * <p>
//     * Note: This function equals the CURTIME() function.
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Time> currentTime() {
//        return new NoParamFunction<>(DictFunctionPostgreSql.CURRENT_TIME, Time.class);
//    }
//
//    /**
//     * The CURRENT_TIME() function returns the current time.
//     * <p>
//     * Note: The time is returned as "HH-MM-SS" (string) or as HHMMSS.uuuuuu (numeric).
//     * <p>
//     * Note: This function equals the CURTIME() function.
//     */
//    default <C extends Date, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> currentTime(Class<C> dateClass) {
//        return new NoParamFunction<>(DictFunctionPostgreSql.CURRENT_TIME, dateClass);
//    }
//
//    /**
//     * The CURRENT_TIMESTAMP() function returns the current date and time.
//     * <p>
//     * Note: The date and time is returned as "YYYY-MM-DD HH-MM-SS" (string) or as YYYYMMDDHHMMSS.uuuuuu (numeric).
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Timestamp> currentTimestamp() {
//        return new NoParamFunction<>(DictFunctionPostgreSql.CURRENT_TIMESTAMP, Timestamp.class);
//    }
//
//    /**
//     * The CURRENT_TIMESTAMP() function returns the current date and time.
//     * <p>
//     * Note: The date and time is returned as "YYYY-MM-DD HH-MM-SS" (string) or as YYYYMMDDHHMMSS.uuuuuu (numeric).
//     */
//    default <C extends Date, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> currentTimestamp(Class<C> dateClass) {
//        return new NoParamFunction<>(DictFunctionPostgreSql.CURRENT_TIMESTAMP, dateClass);
//    }
//
//
//    /**
//     * The LOCALTIME() function returns the current date and time.
//     * <p>
//     * Note: The date and time is returned as "YYYY-MM-DD HH-MM-SS" (string) or as YYYYMMDDHHMMSS.uuuuuu (numeric).
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Time> localTime() {
//        return new NoParamFunction<>(DictFunctionPostgreSql.LOCALTIME, Time.class);
//    }
//
//    /**
//     * The LOCALTIMESTAMP() function returns the current date and time.
//     * <p>
//     * Note: The date and time is returned as "YYYY-MM-DD HH-MM-SS" (string) or as YYYYMMDDHHMMSS.uuuuuu (numeric).
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Timestamp> localTimestamp() {
//        return new NoParamFunction<>(DictFunctionPostgreSql.LOCALTIMESTAMP, Timestamp.class);
//    }
//
//
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, String, R, String> camel(Column<E, String, R> column) {
//        return concat(upper(substring(column, 0, 1)), lower(substring(column, 1, length(column))));
//    }
//
//    default <C extends Number, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> sum(ColumnOrQuery<E, F, R> columnOrQuery) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.SUM);
//    }
//
//    default <C extends Number, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> sum(Class<C> clazz, Column<?, ?, ?>... columns) {
//        return new ColumnFunction<>(DictFunctionPostgreSql.SUM, clazz, "+", columns);
//    }
//
    default <C, E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, C> count(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.COUNT);
    }

    default <E extends Entity, F, R extends RelatedTo> ColumnFunction<E, F, R, Long> count(E entity) {
        return new ColumnFunction<>(DictFunctionPostgreSql.COUNT, Long.class, entity);
    }
//
//    default <C, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> replace(ColumnOrQuery<E, F, R> columnOrQuery) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.REPLACE);
//    }
//
//    //TODO: see substring
//    default <C, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> right(ColumnOrQuery<E, F, R> columnOrQuery, int length) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.RIGHT);
//    }
//
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> distinct(ColumnOrQuery<E, F, R> columnOrQuery) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.DISTINCT);
//    }
//
//
////    default <E extends Entity, F, R extends EntityRelation> IntervalFunction<E, F, R, Long> interval(Number value, DatePeriod addUnit) {
////        return new IntervalFunction<>(DictFunctionPostgreSql.INTERVAL, addUnit, value, Long.class);
////    }
//
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Timestamp> interval(ColumnOrQuery<E, F, R> columnOrQuery, Number value, DatePeriod addUnit) {
//        ColumnFunction<E, F, R, Timestamp> rv = new ColumnFunction<>(columnOrQuery, DictFunctionPostgreSql.INTERVAL, Timestamp.class);
//        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, value);
//        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, addUnit);
//        return rv;
//    }
//
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Timestamp> interval(Number value, DatePeriod addUnit) {
//        NoParamFunction<E, F, R, Timestamp> rv = new NoParamFunction<>(DictFunctionPostgreSql.INTERVAL, Timestamp.class);
//        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, value);
//        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, addUnit);
//        return rv;
//    }

}
