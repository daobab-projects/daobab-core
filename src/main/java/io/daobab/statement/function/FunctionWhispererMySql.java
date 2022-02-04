package io.daobab.statement.function;

import io.daobab.error.MandatoryFunctionParameter;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;
import io.daobab.query.marker.ColumnOrQuery;
import io.daobab.statement.function.base.DatePeriod;
import io.daobab.statement.function.base.FunctionKey;
import io.daobab.statement.function.dictionary.DictFunctionMySql;
import io.daobab.statement.function.type.ColumnFunction;
import io.daobab.statement.function.type.ManyArgumentsFunction;
import io.daobab.statement.function.type.NoParamFunction;
import io.daobab.target.database.query.DataBaseQueryField;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"rawtypes", "unused"})
public interface FunctionWhispererMySql {


    /**
     * Returns the ASCII value for the specific character
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> ascii(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.ASCII, Integer.class);
    }

    /**
     * Returns the length of a string (in characters)
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> charLength(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.CHAR_LENGTH, Integer.class);
    }

    /**
     * Returns the length of a string (in characters)
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> characterLength(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.CHARACTER_LENGTH, Integer.class);
    }

    /**
     * Adds two or more expressions together
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> concat(ColumnOrQuery<?, F, ?>... columns) {
        if (columns == null) throw new MandatoryFunctionParameter(DictFunctionMySql.CONCAT);
        return new ManyArgumentsFunction<>(DictFunctionMySql.CONCAT, columns);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> concat(ColumnOrQuery<E, F, R> columnOrQuery, String str) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.CONCAT, String.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, str);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> concat(String str, ColumnOrQuery<E, F, R> columnOrQuery) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.CONCAT, String.class);
        rv.setKeyValue(ColumnFunction.BEFORE_COL, str);
        return rv;
    }

    /**
     * Adds two or more expressions together with a separator
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> concatWs(String separator, ColumnOrQuery... columns) {
        if (columns == null) throw new MandatoryFunctionParameter(DictFunctionMySql.CONCAT_WS);
        return new ManyArgumentsFunction<>(DictFunctionMySql.CONCAT_WS, separator, columns);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> field(F field, DataBaseQueryField<?, F> query) {
        if (query == null) throw new MandatoryFunctionParameter(DictFunctionMySql.FIELD);
        List<Object> objects = new ArrayList<>();
        objects.add(field);
        objects.add(query);
        return new ManyArgumentsFunction<>(DictFunctionMySql.FIELD, query.getFields().get(0).getColumn().getFieldClass(), objects);
    }

    /**
     * Formats a number to a format like "#,###,###.##", rounded to a specified number of decimal places
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> format(ColumnOrQuery<E, F, R> columnOrQuery, int deciamalPlaces) {
        if (columnOrQuery == null) throw new MandatoryFunctionParameter(DictFunctionMySql.FORMAT);
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.FORMAT);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, deciamalPlaces);
        return rv;
    }

    /**
     * Inserts a string within a string at the specified position and for a certain number of characters
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> insert(ColumnOrQuery<E, F, R> columnOrQuery, int position, int number, String replacement) {
        if (columnOrQuery == null) throw new MandatoryFunctionParameter(DictFunctionMySql.INSERT);
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.INSERT);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, position);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, number);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL3, replacement);
        return rv;
    }

    /**
     * Inserts a string within a string at the specified position and for a certain number of characters
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> instr(ColumnOrQuery<E, F, R> columnOrQuery, String str) {
        if (columnOrQuery == null) throw new MandatoryFunctionParameter(DictFunctionMySql.INSTR);
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.INSTR);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, str);
        return rv;
    }

    /**
     * Converts a string to lower-case
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> lcase(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.LCASE, String.class);
    }

    /**
     * Extracts a number of characters from a string (starting from left)
     *
     * @param number
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> left(ColumnOrQuery<E, F, R> columnOrQuery, int number) {
        if (columnOrQuery == null) throw new MandatoryFunctionParameter(DictFunctionMySql.LEFT);
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.LEFT);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, number);
        return rv;
    }

    /**
     * Returns the length of a string (in bytes)
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> length(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.LENGTH, Integer.class);
    }

    /**
     * Returns the position of the first occurrence of a substring in a string
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> locate(ColumnOrQuery<E, F, R> columnOrQuery, String str, int start) {
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.LOCATE);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, str);
        rv.setKeyValue(ColumnFunction.AFTER_COL2, start);
        return rv;
    }

    /**
     * Converts a string to lower-case
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> lower(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.LOWER, String.class);
    }

    /**
     * Returns the position of the first occurrence of a substring in a string
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> lpad(ColumnOrQuery<E, F, R> columnOrQuery, int length, String lpadString) {
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.LOCATE);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, length);
        rv.setKeyValue(ColumnFunction.AFTER_COL2, lpadString);
        return rv;
    }

    /**
     * Removes leading spaces from a string
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> ltrim(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.LTRIM);
    }

    /**
     * Extracts a substring from a string (starting at any position)
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> mid(ColumnOrQuery<E, F, R> columnOrQuery, int start, int length) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.MID, String.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, start);
        rv.setKeyValue(ColumnFunction.AFTER_COL2, length);
        return rv;
    }

    /**
     * Returns the position of the first occurrence of a substring in a string
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> position(String substring, ColumnOrQuery<E, F, R> columnOrQuery) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.POSITION);
        rv.setMandatoryKeyValue(ColumnFunction.BEFORE_COL2, substring);
        rv.setKeyValue(ColumnFunction.BEFORE_COL, new FunctionKey("IN"));
        return rv;
    }

    /**
     * Repeats a string as many times as specified
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> repeat(ColumnOrQuery<E, F, R> columnOrQuery, Integer number) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.REPEAT);
        rv.setMandatoryKeyValue(ColumnFunction.BEFORE_COL, number);
        return rv;
    }

    /**
     * Replaces all occurrences of a substring within a string, with a new substring
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> replace(ColumnOrQuery<E, F, R> columnOrQuery, String fromString, String newString) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.REPLACE);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, fromString);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, newString);
        return rv;
    }

    /**
     * Reverses a string and returns the result
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> reverse(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.REVERSE, String.class);
    }

    /**
     * Extracts a number of characters from a string (starting from right)
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> right(ColumnOrQuery<E, F, R> columnOrQuery, Integer length, String rpadString) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.RIGHT);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, length);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, rpadString);
        return rv;
    }

    /**
     * Right-pads a string with another string, to a certain length
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> rpad(ColumnOrQuery<E, F, R> columnOrQuery, int length, String rpadString) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.RPAD);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, length);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, rpadString);
        return rv;
    }

    /**
     * Removes trailing spaces from a string
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> rtrim(String value) {
        return new ColumnFunction<>(DictFunctionMySql.RTRIM, String.class, value);
    }

    /**
     * Right-pads a string with another string, to a certain length
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> strcmp(ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<E, F, R> columnOrQuery2) {
        ColumnFunction<E, F, R, Integer> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.RPAD);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, columnOrQuery2);
        return rv;
    }

    /**
     * Extracts a substring from a string (starting at any position)
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> substr(ColumnOrQuery<E, F, R> columnOrQuery, int start, int length) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.SUBSTR);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, start);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, length);
        return rv;
    }

    /**
     * Extracts a substring from a string (starting at any position)
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> substring(ColumnOrQuery<E, F, R> columnOrQuery, Number from, Number to) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.SUBSTRING);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, from);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, to);
        return rv;
    }

    /**
     * Extracts a substring from a string (starting at any position)
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> substring(ColumnOrQuery<E, F, R> columnOrQuery, int from, Column<?, ? extends Number, ?> to) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.SUBSTRING);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, from);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, to);
        return rv;
    }

    /**
     * Extracts a substring from a string (starting at any position)
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> substring(ColumnOrQuery<E, F, R> columnOrQuery, Column<?, Number, ?> from, Column<?, Number, ?> to) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.SUBSTRING);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, from);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, to);
        return rv;
    }

    /**
     * Removes leading and trailing spaces from a string
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> trim(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.TRIM, String.class);
    }

    /**
     * Converts a string to upper-case
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> upper(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.UPPER, String.class);
    }


    /**
     * The ABS() function returns the absolute (positive) value of a number.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> abs(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.ABS);
    }

    /**
     * The ACOS() function returns the arc cosine of a number.
     * The specified number must be between -1 to 1, otherwise this function returns NULL.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> acos(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.ACOS, BigDecimal.class);
    }

    /**
     * The ASIN() function returns the arc sine of a number.
     * <p>
     * The specified number must be between -1 to 1, otherwise this function returns NULL.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> asin(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.ASIN, BigDecimal.class);
    }

    /**
     * The ATAN() function returns the arc tangent of one or two numbers.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> atan(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.ATAN, BigDecimal.class);
    }

    /**
     *
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> atan(ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<E, F, R> columnOrQuery2) {
        ColumnFunction<E, F, R, BigDecimal> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.ATAN, BigDecimal.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, columnOrQuery2);
        return rv;
    }

    /**
     * The ATAN() function returns the arc tangent of one or two numbers.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> atan2(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.ATAN2, BigDecimal.class);
    }

    /**
     * The ATAN2() function returns the arc tangent of two numbers.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> atan2(ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<E, F, R> columnOrQuery2) {
        ColumnFunction<E, F, R, BigDecimal> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.ATAN2, BigDecimal.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, columnOrQuery2);
        return rv;
    }

    /**
     * The AVG() function returns the average value of an expression.
     * <p>
     * Note: NULL values are ignored.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> avg(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.AVG, BigDecimal.class);
    }

    /**
     * The CEIL() function returns the smallest integer value that is bigger than or equal to a number.
     * <p>
     * Note: This function is equal to the CEILING() function.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> ceil(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.CEIL, Integer.class);
    }

    /**
     * The CEILING() function returns the smallest integer value that is bigger than or equal to a number.
     * <p>
     * Note: This function is equal to the CEIL() function.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> ceiling(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.CEILING, Integer.class);
    }

    /**
     * The COS() function returns the cosine of a number.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> cos(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.COS, BigDecimal.class);
    }

    /**
     * The COT() function returns the cotangent of a number.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> cot(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.COT, BigDecimal.class);
    }

    //    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> count(ColumnOrQuery<E, F, R> columnOrQuery) {
//        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.COUNT);
//    }

    /**
     * The DEGREES() function converts a value in radians to degrees.
     * <p>
     * Note: See also the RADIANS() and PI() functions.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> degrees(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.DEGREES, BigDecimal.class);
    }

    /**
     * The DIV function is used for integer division (x is divided by y). An integer value is returned.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> div(ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<E, F, R> columnOrQuery2) {
        ColumnFunction<E, F, R, BigDecimal> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.DIV, BigDecimal.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, new FunctionKey(" DIV "));
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, columnOrQuery2);
        return rv;
    }

    /**
     * The DIV function is used for integer division (x is divided by y). An integer value is returned.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> div(ColumnOrQuery<E, F, R> columnOrQuery, Number number) {
        ColumnFunction<E, F, R, BigDecimal> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.DIV, BigDecimal.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, new FunctionKey(" DIV "));
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, number);
        return rv;
    }

    /**
     * The DIV function is used for integer division (x is divided by y). An integer value is returned.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> div(Number number, ColumnOrQuery<E, F, R> columnOrQuery2) {
        ColumnFunction<E, F, R, BigDecimal> rv = new ColumnFunction<>(columnOrQuery2, DictFunctionMySql.DIV, BigDecimal.class);
        rv.setMandatoryKeyValue(ColumnFunction.BEFORE_COL, new FunctionKey(" DIV "));
        rv.setMandatoryKeyValue(ColumnFunction.BEFORE_COL2, number);
        return rv;
    }

    /**
     * The EXP() function returns e raised to the power of the specified number.
     * <p>
     * The constant e (2.718281...), is the base of natural logarithms.
     * <p>
     * Tip: Also look at the LOG() and LN() functions.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> exp(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.EXP, BigDecimal.class);
    }

    /**
     * The FLOOR() function returns the largest integer value that is smaller than or equal to a number.
     * <p>
     * Note: Also look at the ROUND(), CEIL(), CEILING(), TRUNCATE(), and DIV functions.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> floor(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.FLOOR);
    }

    /**
     * The GREATEST() function returns the greatest value of the list of arguments.
     * <p>
     * Note: See also the LEAST() function.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> greatest(ColumnOrQuery<?, ?, ?>... columns) {
        return new ManyArgumentsFunction<>(DictFunctionMySql.GREATEST, columns);
    }

    /**
     * The LEAST() function returns the smallest value of the list of arguments.
     * <p>
     * Note: See also the GREATEST() function.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> least(ColumnOrQuery<?, F, ?>... columns) {
        return new ManyArgumentsFunction<>(DictFunctionMySql.LEAST, columns);
    }

    /**
     * The LN() function returns the natural logarithm of a number.
     * <p>
     * Note: See also the LOG() and EXP() functions.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> ln(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.LN, BigDecimal.class);
    }

    /**
     * The LOG() function returns the natural logarithm of a specified number, or the logarithm of the number to the specified base.
     * <p>
     * Note: See also the LN() and EXP() functions.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> log(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.LOG, BigDecimal.class);
    }

    /**
     * The LOG10() function returns the natural logarithm of a number to base-10.
     * <p>
     * Note: See also the LOG() function.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> log10(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.LOG10, BigDecimal.class);
    }

    /**
     * The LOG2() function returns the natural logarithm of a number to base-2.
     * <p>
     * Note: See also the LOG() function.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> log2(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.LOG2, BigDecimal.class);
    }

    /**
     * The MAX() function returns the maximum value in a set of values.
     * <p>
     * Note: See also the MIN() function.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> max(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.MAX);
    }

    /**
     * The MIN() function returns the minimum value in a set of values.
     * <p>
     * Note: See also the MAX() function.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> min(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.MIN);
    }

    /**
     * The MOD() function returns the remainder of a number divided by another number.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> mod(ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<E, F, R> columnOrQuery2) {
        ColumnFunction<E, F, R, BigDecimal> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.MOD);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, new FunctionKey(" MOD "));
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, columnOrQuery2);
        return rv;
    }

    /**
     * The MOD() function returns the remainder of a number divided by another number.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> mod(ColumnOrQuery<E, F, R> columnOrQuery, Number number) {
        ColumnFunction<E, F, R, BigDecimal> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.MOD);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, new FunctionKey(" MOD "));
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, number);
        return rv;
    }

    /**
     * The MOD() function returns the remainder of a number divided by another number.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> mod(Number number, ColumnOrQuery<E, F, R> columnOrQuery2) {
        ColumnFunction<E, F, R, BigDecimal> rv = new ColumnFunction<>(columnOrQuery2, DictFunctionMySql.MOD);
        rv.setMandatoryKeyValue(ColumnFunction.BEFORE_COL, new FunctionKey(" MOD "));
        rv.setMandatoryKeyValue(ColumnFunction.BEFORE_COL2, columnOrQuery2);
        return rv;
    }

    /**
     * The PI() function returns the value of PI.
     * <p>
     * Note: See also the DEGREES() and RADIANS() functions.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> pi() {
        return new NoParamFunction<>(DictFunctionMySql.PI, BigDecimal.class);
    }

    /**
     * The POW() function returns the value of a number raised to the power of another number.
     * <p>
     * Note: This function is equal to the POWER() function.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> pow(ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<E, F, R> columnOrQuery2) {
        ColumnFunction<E, F, R, BigDecimal> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.POW);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, columnOrQuery2);
        return rv;
    }

    /**
     * The POWER() function returns the value of a number raised to the power of another number.
     * <p>
     * Note: This function is equal to the POW() function.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> power(ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<E, F, R> columnOrQuery2) {
        ColumnFunction<E, F, R, BigDecimal> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.POWER);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, columnOrQuery2);
        return rv;
    }

    /**
     * The RADIANS() function converts a degree value into radians.
     * <p>
     * Note: See also the DEGREES() and PI() functions.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> radians(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.RADIANS, BigDecimal.class);
    }

    /**
     * The RAND() function returns a random number between 0 (inclusive) and 1 (exclusive).
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> rand(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.RAND, BigDecimal.class);
    }

    /**
     * The ROUND() function rounds a number to a specified number of decimal places.
     * <p>
     * Note: See also the FLOOR(), CEIL(), CEILING(), and TRUNCATE() functions.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> round(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.ROUND);
    }

    /**
     * The ROUND() function rounds a number to a specified number of decimal places.
     * <p>
     * Note: See also the FLOOR(), CEIL(), CEILING(), and TRUNCATE() functions.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> round(ColumnOrQuery<E, F, R> columnOrQuery, Integer decimals) {
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.ROUND);
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
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.SIGN, Integer.class);
    }

    /**
     * The SIN() function returns the sine of a number.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> sin(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.SIN, BigDecimal.class);
    }

    /**
     * The SQRT() function returns the square root of a number.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> sqrt(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.SQRT, BigDecimal.class);
    }

    /**
     * The SUM() function calculates the sum of a set of values.
     * <p>
     * Note: NULL values are ignored.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> sum(ColumnOrQuery<?, F, ?>... columns) {
        return new ManyArgumentsFunction<>(DictFunctionMySql.SUM, BigDecimal.class, "+", columns);
    }

    /**
     * The TAN() function returns the tangent of a number.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> tan(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.TAN, BigDecimal.class);
    }

    /**
     * The TRUNCATE() function truncates a number to the specified number of decimal places.
     * <p>
     * Note: See also the FLOOR(), CEIL(), CEILING(), and ROUND() functions.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> truncate(ColumnOrQuery<E, F, R> columnOrQuery, Integer decimals) {
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.TRUNCATE);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, decimals);
        return rv;
    }

    /**
     * The ADDDATE() function adds a time/date interval to a date and then returns the date.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> addDate(ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<?, ?, ?> interval) {
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.ADDDATE);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, interval);
        return rv;
    }

    /**
     * The ADDDATE() function adds a time/date interval to a date and then returns the date.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> addDate(ColumnOrQuery<E, F, R> columnOrQuery, Integer days) {
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.ADDDATE);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, days);
        return rv;
    }

    /**
     * The ADDTIME() function adds a time interval to a time/datetime and then returns the time/datetime.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> addTime(ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<?, ?, ?> column2) {
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.ADDTIME);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, column2);
        return rv;
    }

    /**
     * The ADDTIME() function adds a time interval to a time/datetime and then returns the time/datetime.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> addTime(ColumnOrQuery<E, F, R> columnOrQuery, String timeString) {
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.ADDTIME);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, timeString);
        return rv;
    }

    /**
     * The CURDATE() function returns the current date.
     * <p>
     * Note: The date is returned as "YYYY-MM-DD" (string) or as YYYYMMDD (numeric).
     * <p>
     * Note: This function equals the CURRENT_DATE() function.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, java.sql.Date> curDate() {
        return new NoParamFunction<>(DictFunctionMySql.CURDATE, java.sql.Date.class);
    }


    /**
     * The CURRENT_DATE() function returns the current date.
     * <p>
     * Note: The date is returned as "YYYY-MM-DD" (string) or as YYYYMMDD (numeric).
     * <p>
     * Note: This function equals the CURDATE() function.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, java.sql.Date> curretDate() {
        return new NoParamFunction<>(DictFunctionMySql.CURRENT_DATE, java.sql.Date.class);
    }

    /**
     * The CURRENT_DATE() function returns the current date.
     * <p>
     * Note: The date is returned as "YYYY-MM-DD" (string) or as YYYYMMDD (numeric).
     * <p>
     * Note: This function equals the CURDATE() function.
     */
    default <C extends java.sql.Date, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> curretDate(Class<C> dateClass) {
        return new NoParamFunction<>(DictFunctionMySql.CURRENT_DATE, dateClass);
    }

    /**
     * The CURRENT_TIME() function returns the current time.
     * <p>
     * Note: The time is returned as "HH-MM-SS" (string) or as HHMMSS.uuuuuu (numeric).
     * <p>
     * Note: This function equals the CURTIME() function.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Time> currentTime() {
        return new NoParamFunction<>(DictFunctionMySql.CURRENT_TIME, Time.class);
    }

    /**
     * The CURRENT_TIME() function returns the current time.
     * <p>
     * Note: The time is returned as "HH-MM-SS" (string) or as HHMMSS.uuuuuu (numeric).
     * <p>
     * Note: This function equals the CURTIME() function.
     */
    default <C extends java.sql.Date, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> currentTime(Class<C> dateClass) {
        return new NoParamFunction<>(DictFunctionMySql.CURRENT_TIME, dateClass);
    }

    /**
     * The CURRENT_TIMESTAMP() function returns the current date and time.
     * <p>
     * Note: The date and time is returned as "YYYY-MM-DD HH-MM-SS" (string) or as YYYYMMDDHHMMSS.uuuuuu (numeric).
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Timestamp> currentTimestamp() {
        return new NoParamFunction<>(DictFunctionMySql.CURRENT_TIMESTAMP, Timestamp.class);
    }

    /**
     * The CURRENT_TIMESTAMP() function returns the current date and time.
     * <p>
     * Note: The date and time is returned as "YYYY-MM-DD HH-MM-SS" (string) or as YYYYMMDDHHMMSS.uuuuuu (numeric).
     */
    default <C extends java.sql.Date, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> currentTimestamp(Class<C> dateClass) {
        return new NoParamFunction<>(DictFunctionMySql.CURRENT_TIMESTAMP, dateClass);
    }

    /**
     * The CURTIME() function returns the current time.
     * <p>
     * Note: The time is returned as "HH-MM-SS" (string) or as HHMMSS.uuuuuu (numeric).
     * <p>
     * Note: This function equals the CURRENT_TIME() function.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Time> curtime() {
        return new NoParamFunction<>(DictFunctionMySql.CURTIME, Time.class);
    }

    /**
     * The DATE() function extracts the date part from a datetime expression.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, java.sql.Date> date(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.DATE, java.sql.Date.class);
    }

    /**
     * The DATEDIFF() function returns the number of days between two date values.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Long> dateDiff(ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<E, F, R> columnOrQuery2) {
        ColumnFunction<E, F, R, Long> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.DATEDIFF, Long.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, columnOrQuery2);
        return rv;
    }

    /**
     * The DATE_ADD() function adds a time/date interval to a date and then returns the date.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> dateAdd(ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<?, ?, ?> interval) {
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.DATE_ADD);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, interval);
        return rv;
    }

    /**
     * The DATE_FORMAT() function formats a date as specified.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> dateFormat(ColumnOrQuery<E, F, R> columnOrQuery, String format) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.DATE_FORMAT, String.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, format);
        return rv;
    }

    /**
     * The DATE_SUB() function subtracts a time/date interval from a date and then returns the date.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> dateSub(ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<?, ?, ?> interval) {
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.DATE_SUB);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, interval);
        return rv;
    }

    /**
     * The DAY() function returns the day of the month for a given date (a number from 1 to 31).
     * <p>
     * Note: This function equals the DAYOFMONTH() function.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> day(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.DAY, Integer.class);
    }

    /**
     * The DAYNAME() function returns the weekday name for a given date.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> dayName(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.DAYNAME, String.class);
    }

    /**
     * The DAYOFMONTH() function returns the day of the month for a given date (a number from 1 to 31).
     * <p>
     * Note: This function equals the DAY() function.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> dayOfMonth(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.DAYOFMONTH, Integer.class);
    }

    /**
     * The DAYOFWEEK() function returns the weekday index for a given date (a number from 1 to 7).
     * <p>
     * Note: 1=Sunday, 2=Monday, 3=Tuesday, 4=Wednesday, 5=Thursday, 6=Friday, 7=Saturday.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> dayOfWeek(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.DAYOFWEEK, Integer.class);
    }

    /**
     * The DAYOFYEAR() function returns the day of the year for a given date (a number from 1 to 366).
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> dayOfYear(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.DAYOFYEAR, Integer.class);
    }

    /**
     * The EXTRACT() function extracts a part from a given date.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> extract(DatePeriod period, ColumnOrQuery<E, F, R> columnOrQuery) {
        ColumnFunction<E, F, R, Integer> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.EXTRACT, Integer.class);
        rv.setMandatoryKeyValue(ColumnFunction.BEFORE_COL2, period);
        rv.setMandatoryKeyValue(ColumnFunction.BEFORE_COL, new FunctionKey(" from "));
        return rv;
    }

    /**
     * The FROM_DAYS() function returns a date from a numeric datevalue.
     * <p>
     * The FROM_DAYS() function is to be used only with dates within the Gregorian calendar.
     * <p>
     * Note: This function is the opposite of the TO_DAYS() function.
     */
    //TODO: check it
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Date> fromDays(Long number) {
        ColumnFunction<E, F, R, Date> rv = new NoParamFunction<>(DictFunctionMySql.FROM_DAYS, Date.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, number);
        return rv;
    }

    /**
     * The HOUR() function returns the hour part for a given date (from 0 to 838).
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> hour(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.HOUR, Integer.class);
    }

    /**
     * The LAST_DAY() function extracts the last day of the month for a given date.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> lastDay(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.LAST_DAY, Integer.class);
    }

    /**
     * The LOCALTIME() function returns the current date and time.
     * <p>
     * Note: The date and time is returned as "YYYY-MM-DD HH-MM-SS" (string) or as YYYYMMDDHHMMSS.uuuuuu (numeric).
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Time> localTime() {
        return new NoParamFunction<>(DictFunctionMySql.LOCALTIME, Time.class);
    }

    /**
     * The LOCALTIMESTAMP() function returns the current date and time.
     * <p>
     * Note: The date and time is returned as "YYYY-MM-DD HH-MM-SS" (string) or as YYYYMMDDHHMMSS.uuuuuu (numeric).
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Timestamp> localTimestamp() {
        return new NoParamFunction<>(DictFunctionMySql.LOCALTIMESTAMP, Timestamp.class);
    }

    /**
     * The MAKEDATE() function creates and returns a date based on a year and a number of days value.
     */
    //TODO: check it
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Date> makeDate(int year, int day) {
        ColumnFunction<E, F, R, Date> rv = new NoParamFunction<>(DictFunctionMySql.MAKEDATE, Date.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, year);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, day);
        return rv;
    }

    /**
     * The MAKETIME() function creates and returns a time based on an hour, minute, and second value.
     */
    //TODO: check it
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Time> makeTime(int hour, int minute, int second) {
        ColumnFunction<E, F, R, Time> rv = new NoParamFunction<>(DictFunctionMySql.MAKETIME, Time.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, hour);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, minute);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL3, second);
        return rv;
    }

    /**
     * The MICROSECOND() function returns the microsecond part of a time/datetime (from 0 to 999999).
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> microSecond(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.MICROSECOND, Integer.class);
    }

    /**
     * The MINUTE() function returns the minute part of a time/datetime (from 0 to 59).
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> minute(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.MINUTE, Integer.class);
    }

    /**
     * The MONTH() function returns the month part for a given date (a number from 1 to 12).
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> month(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.MONTH, Integer.class);
    }

    /**
     * The MONTHNAME() function returns the name of the month for a given date.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> monthName(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.MONTHNAME, String.class);
    }

    /**
     * The NOW() function returns the current date and time.
     * <p>
     * Note: The date and time is returned as "YYYY-MM-DD HH-MM-SS" (string) or as YYYYMMDDHHMMSS.uuuuuu (numeric).
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Timestamp> now() {
        return new NoParamFunction<>(DictFunctionMySql.NOW, Timestamp.class);
    }

    /**
     * The PERIOD_ADD() function adds a specified number of months to a period.
     * <p>
     * The PERIOD_ADD() function will return the result formatted as YYYYMM.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Timestamp> periodAdd(ColumnOrQuery<E, F, R> columnOrQuery, int number) {
        ColumnFunction<E, F, R, Timestamp> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.PERIOD_ADD, Timestamp.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, number);
        return rv;
    }

    /**
     * The PERIOD_DIFF() function returns the difference between two periods. The result will be in months.
     * <p>
     * Note: period1 and period2 should be in the same format.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Timestamp> periodDiff(ColumnOrQuery<E, F, R> columnOrQuery, Column<?, F, ?> column2) {
        ColumnFunction<E, F, R, Timestamp> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.PERIOD_DIFF, Timestamp.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, Timestamp.class);
        return rv;
    }

    /**
     * The QUARTER() function returns the quarter of the year for a given date value (a number from 1 to 4).
     * <p>
     * January-March returns 1
     * April-June returns 2
     * July-Sep returns 3
     * Oct-Dec returns 4
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> quarter(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.QUARTER, Integer.class);
    }

    /**
     * The SECOND() function returns the seconds part of a time/datetime (from 0 to 59).
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> second(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.SECOND, Integer.class);
    }

    /**
     * The SEC_TO_TIME() function returns a time value (in format HH:MM:SS) based on the specified seconds.
     */
    default <E extends Entity, F extends Number, R extends EntityRelation> ColumnFunction<E, F, R, Timestamp> secToTime(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.SEC_TO_TIME);
    }

    /**
     * The STR_TO_DATE() function returns a date based on a string and a format.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Date> strToDate(ColumnOrQuery<E, F, R> columnOrQuery, String format) {
        ColumnFunction<E, F, R, Date> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.STR_TO_DATE, Date.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, format);
        return rv;
    }

    /**
     * The SUBDATE() function subtracts a time/date interval from a date and then returns the date.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> subDate(ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<?, ?, ?> interval) {
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.SUBDATE);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, interval);
        return rv;
    }

    /**
     * The SUBTIME() function subtracts time from a time/datetime expression and then returns the new time/datetime.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> subTime(ColumnOrQuery<E, F, R> columnOrQuery, String timeInterval) {
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.SUBTIME);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, timeInterval);
        return rv;
    }

    /**
     * The SYSDATE() function returns the current date and time.
     * <p>
     * Note: The date and time is returned as "YYYY-MM-DD HH:MM:SS" (string) or as YYYYMMDDHHMMSS (numeric).
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Timestamp> sysDate() {
        return new NoParamFunction<>(DictFunctionMySql.SYSDATE, Timestamp.class);
    }

    /**
     * The TIME() function extracts the time part from a given time/datetime.
     * <p>
     * Note: This function returns "00:00:00" if expression is not a datetime/time, or NULL if expression is NULL.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Time> time(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.TIME, Time.class);
    }

    /**
     * The TIME_FORMAT() function formats a time by a specified format.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> timeFormat(ColumnOrQuery<E, F, R> columnOrQuery, String format) {
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.TIME_FORMAT);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, format);
        return rv;
    }

    /**
     * The TIME_TO_SEC() function converts a time value into seconds.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Long> timeToSec(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.TIME_TO_SEC, Long.class);
    }

    /**
     * The TIMEDIFF() function returns the difference between two time/datetime expressions.
     * <p>
     * Note: time1 and time2 should be in the same format, and the calculation is time1 - time2.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Long> timeDiff(ColumnOrQuery<E, F, R> columnOrQuery, ColumnOrQuery<E, F, R> columnOrQuery2) {
        ColumnFunction<E, F, R, Long> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.TIMEDIFF, Long.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, columnOrQuery2);
        return rv;
    }

    /**
     * The TIMESTAMP() function returns a datetime value based on a date or datetime value.
     * <p>
     * Note: If there are specified two arguments with this function, it first adds the second argument to the first, and then returns a datetime value.
     */
    default <E extends Entity, F extends Date, R extends EntityRelation> ColumnFunction<E, F, R, Timestamp> timestamp(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.TIMESTAMP, Timestamp.class);
    }

    /**
     * The TIMESTAMP() function returns a datetime value based on a date or datetime value.
     * <p>
     * Note: If there are specified two arguments with this function, it first adds the second argument to the first, and then returns a datetime value.
     */
    default <E extends Entity, F extends Date, R extends EntityRelation> ColumnFunction<E, F, R, Timestamp> timestamp(ColumnOrQuery<E, F, R> columnOrQuery, String time) {
        ColumnFunction<E, F, R, Timestamp> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.TIMESTAMP, Timestamp.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, time);
        return rv;
    }

    /**
     * The TO_DAYS() function returns the number of days between a date and year 0 (date "0000-00-00").
     * <p>
     * The TO_DAYS() function can be used only with dates within the Gregorian calendar.
     * <p>
     * Note: This function is the opposite of the FROM_DAYS() function.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Long> toDays(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.TO_DAYS, Long.class);
    }

    /**
     * The WEEK() function returns the week number for a given date (a number from 0 to 53).
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> week(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.WEEK, Integer.class);
    }

    /**
     * The WEEKDAY() function returns the weekday number for a given date.
     * <p>
     * Note: 0 = Monday, 1 = Tuesday, 2 = Wednesday, 3 = Thursday, 4 = Friday, 5 = Saturday, 6 = Sunday.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> weekDay(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.WEEKDAY, Integer.class);
    }

    /**
     * The WEEKOFYEAR() function returns the week number for a given date (a number from 1 to 53).
     * <p>
     * Note: This function assumes that the first day of the week is Monday and the first week of the year has more than 3 days.
     * <p>
     * Tip: Also look at the WEEK() function.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> weekOfYear(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.WEEKOFYEAR, Integer.class);
    }

    /**
     * The YEAR() function returns the year part for a given date (a number from 1000 to 9999).
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> year(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.YEAR, Integer.class);
    }

    /**
     * The YEARWEEK() function returns the year and week number (a number from 0 to 53) for a given date.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> yearWeek(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.YEARWEEK, Integer.class);
    }


    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, String, R, String> camel(Column<E, String, R> column) {
        return concat(upper(substring(column, 0, 1)), lower(substring(column, 1, length(column))));
    }

    default <C extends Number, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> sum(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.SUM);
    }

    default <C extends Number, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> sum(Class<C> clazz, Column<?, ?, ?>... columns) {
        return new ColumnFunction<>(DictFunctionMySql.SUM, clazz, "+", columns);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Long> count(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.COUNT);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Long> count(E entity) {
        return new ColumnFunction<>(DictFunctionMySql.COUNT, Long.class, entity);
    }

    default <C, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> replace(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.REPLACE);
    }

    //TODO: see substring
    default <C, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> right(ColumnOrQuery<E, F, R> columnOrQuery, int length) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.RIGHT);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> distinct(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionMySql.DISTINCT);
    }


//    default <E extends Entity, F, R extends EntityRelation> IntervalFunction<E, F, R, Long> interval(Number value, DatePeriod addUnit) {
//        return new IntervalFunction<>(DictFunctionMySql.INTERVAL, addUnit, value, Long.class);
//    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Timestamp> interval(ColumnOrQuery<E, F, R> columnOrQuery, Number value, DatePeriod addUnit) {
        ColumnFunction<E, F, R, Timestamp> rv = new ColumnFunction<>(columnOrQuery, DictFunctionMySql.INTERVAL, Timestamp.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, value);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, addUnit);
        return rv;
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Timestamp> interval(Number value, DatePeriod addUnit) {
        NoParamFunction<E, F, R, Timestamp> rv = new NoParamFunction<>(DictFunctionMySql.INTERVAL, Timestamp.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, value);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, addUnit);
        return rv;
    }
//
//    default <E extends Entity, F extends Date, R extends EntityRelation> IntervalFunction<E, F, R, F> interval(int number, DatePeriod period) {
//        return new IntervalFunction<>(DictFunctionMySql.INTERVAL, period, number);
//    }
}
