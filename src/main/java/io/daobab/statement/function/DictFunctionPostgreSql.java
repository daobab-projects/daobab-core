package io.daobab.statement.function;

public interface DictFunctionPostgreSql {

//    String Functions

    //    Returns numeric value of left-most character
    String ASCII = "ASCII";

    //    Returns length of argument in bits
    String BIT_LENGTH = "BIT_LENGTH";

    //    Returns number of characters in argument
    String CHAR_LENGTH = "CHAR_LENGTH";

    //    A synonym for CHAR_LENGTH="";
    String CHARACTER_LENGTH = "CHARACTER_LENGTH";

    //    Returns concatenate with separator
    String CONCAT_WS = "CONCAT_WS";

    //    Returns concatenated string
    String CONCAT = "CONCAT";

    //    Synonym for LOWER="";
    String LCASE = "LCASE";

    //    Returns the leftmost number of characters as specified
    String LEFT = "LEFT";

    //    Returns the length of a string in bytes
    String LENGTH = "LENGTH";

    //    Returns the argument in lowercase
    String LOWER = "LOWER";

    //    Returns the string argument, left-padded with the specified string
    String LPAD = "LPAD";

    //    Removes leading spaces
    String LTRIM = "LTRIM";

    //    Returns a substring starting from the specified position
    String MID = "MID";

    //    A synonym for LOCATE="";
    String POSITION = "POSITION";

    //    Escapes the argument for use in an SQL statement
    String QUOTE = "QUOTE";

    //    Pattern matching using regular expressions
    String REGEXP = "REGEXP";

    //    Repeats a string the specified number of times
    String REPEAT = "REPEAT";

    //    Replaces occurrences of a specified string
    String REPLACE = "REPLACE";

    //    Reverse the characters in a string
    String REVERSE = "REVERSE";

    //    Returns the specified rightmost number of characters
    String RIGHT = "RIGHT";

    //    Appends string the specified number of times
    String RPAD = "RPAD";

    //    Removes trailing spaces
    String RTRIM = "RTRIM";

    //    Returns the substring as specified
    String SUBSTR = "SUBSTR";
    String SUBSTRING = "SUBSTRING";

    //    Removes leading and trailing spaces
    String TRIM = "TRIM";

    //    Synonym for UPPER="";
    String UCASE = "UCASE";

    //    Converts to uppercase
    String UPPER = "UPPER";


//    Numeric Function

    String ABS = "ABS";
//    Returns the absolute value of numeric expression.

    String ACOS = "ACOS";
//    Returns the arccosine of numeric expression. Returns NULL if the value is not in the range -1 to 1.

    String ASIN = "ASIN";
//    Returns the arcsine of numeric expression. Returns NULL if value is not in the range -1 to 1

    String ATAN = "ATAN";
//    Returns the arctangent of numeric expression.

    String ATAN2 = "ATAN2";
//    Returns the arctangent of the two variables passed to it.

    String CEIL = "CEIL";
//    Returns the smallest integer value that is not less than passed numeric expression

    String CEILING = "CEILING";
//    Returns the smallest integer value that is not less than passed numeric expression

    String COS = "COS";
//    Returns the cosine of passed numeric expression. The numeric expression should be expressed in radians.

    String COT = "COT";
//    Returns the cotangent of passed numeric expression.

    String DEGREES = "DEGREES";
//    Returns numeric expression converted from radians to degrees.

    String EXP = "EXP";
//    Returns the base of the natural logarithm (e) raised to the power of passed numeric expression.

    String FLOOR = "FLOOR";
//    Returns the largest integer value that is not greater than passed numeric expression.

    String GREATEST = "GREATEST";
//    Returns the largest value of the input expressions.

    String LEAST = "LEAST";
//    Returns the minimum-valued input when given two or more.

    String LOG = "LOG";
//    Returns the natural logarithm of the passed numeric expression.

    String MOD = "MOD";
//    Returns the remainder of one expression by diving by another expression.

    String PI = "PI";
//    Returns the value of pi

    String POW = "POW";
//    Returns the value of one expression raised to the power of another expression

    String POWER = "POWER";
//    Returns the value of one expression raised to the power of another expression

    String RADIANS = "RADIANS";
//    Returns the value of passed expression converted from degrees to radians.

    String ROUND = "ROUND";
//    Returns numeric expression rounded to an integer. Can be used to round an expression to a number of decimal points

    String SIN = "sin";
//    Returns the sine of numeric expression given in radians.

    String SQRT = "sqrt";
//    Returns the non-negative square root of numeric expression.

    String TAN = "tan";
//    Returns the tangent of numeric expression expressed in radians.


    /**
     * PostgreSQL ARRAY_AGG function is used to concatenate the input values including null into an array.
     */
    String ARRAY_AGG = "ARRAY_AGG";


    String SUM = "SUM";

    String AVG = "AVG";

    String MIN = "MIN";

    String MAX = "MAX";


    //    Delivers current date.
    String CURRENT_DATE = "CURRENT_DATE";

    //    Delivers values with time zone.
    String CURRENT_TIME = "CURRENT_TIME";

    //    Delivers values with time zone.
    String CURRENT_TIMESTAMP = "CURRENT_TIMESTAMP";

    //    Delivers values without time zone.
    String LOCALTIME = "LOCALTIME";

    //    Delivers values without time zone.
    String LOCALTIMESTAMP = "LOCALTIMESTAMP";

    String AGE = "AGE";


    String COUNT = "COUNT";
    String INTERVAL = "INTERVAL";
    String DISTINCT = "DISTINCT";

}
