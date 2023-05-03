package io.daobab.statement.function.dictionary;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings("unused")
public interface DictFunctionMySql {

    String INTERVAL = "INTERVAL";
    String DISTINCT = "DISTINCT";

    //MySQL String Functions
    //Returns the ASCII value for the specific character
    String ASCII = "ASCII";
    //Returns the length of a string (in characters)
    String CHAR_LENGTH = "CHAR_LENGTH";
    //Returns the length of a string (in characters)
    String CHARACTER_LENGTH = "CHARACTER_LENGTH";
    //Adds two or more expressions together
    String CONCAT = "CONCAT";
    //Adds two or more expressions together with a separator
    String CONCAT_WS = "CONCAT_WS";
    //Returns the index position of a value in a list of values
    String FIELD = "FIELD";
    //Returns the position of a string within a list of strings
    String FIND_IN_SET = "FIND_IN_SET";
    //Formats a number to a format like "#,###,###.##", rounded to a specified number of decimal places
    String FORMAT = "FORMAT";
    //Inserts a string within a string at the specified position and for a certain number of characters
    String INSERT = "INSERT";
    //Returns the position of the first occurrence of a string in another string
    String INSTR = "INSTR";
    //Converts a string to lower-case
    String LCASE = "LCASE";
    //Extracts a number of characters from a string (starting from left)
    String LEFT = "LEFT";
    //Returns the length of a string (in bytes)
    String LENGTH = "LENGTH";
    //Returns the position of the first occurrence of a substring in a string
    String LOCATE = "LOCATE";
    //Converts a string to lower-case
    String LOWER = "LOWER";
    //Left-pads a string with another string, to a certain length
    String LPAD = "LPAD";
    //Removes leading spaces from a string
    String LTRIM = "LTRIM";
    //Extracts a substring from a string (starting at any position)
    String MID = "MID";
    //Returns the position of the first occurrence of a substring in a string
    String POSITION = "POSITION";
    //Repeats a string as many times as specified
    String REPEAT = "REPEAT";
    //Replaces all occurrences of a substring within a string, with a new substring
    String REPLACE = "REPLACE";
    //Reverses a string and returns the result
    String REVERSE = "REVERSE";
    //Extracts a number of characters from a string (starting from right)
    String RIGHT = "RIGHT";
    //Right-pads a string with another string, to a certain length
    String RPAD = "RPAD";
    //Removes trailing spaces from a string
    String RTRIM = "RTRIM";
    //Returns a string of the specified number of space characters
    String SPACE = "SPACE";
    //Compares two strings
    String STRCMP = "STRCMP";
    //Extracts a substring from a string (starting at any position)
    String SUBSTR = "SUBSTR";
    //Extracts a substring from a string (starting at any position)
    String SUBSTRING = "SUBSTRING";
    //Returns a substring of a string before a specified number of delimiter occurs
    String SUBSTRING_INDEX = "SUBSTRING_INDEX";
    //Removes leading and trailing spaces from a string
    String TRIM = "TRIM";
    //Converts a string to upper-case
    String UCASE = "UCASE";
    //Converts a string to upper-case
    String UPPER = "UPPER";

    //    MySQL Numeric Functions
    String ABS = "ABS";//Returns the absolute value of a number
    String ACOS = "ACOS";//Returns the arc cosine of a number
    String ASIN = "ASIN";//Returns the arc sine of a number
    String ATAN = "ATAN";//Returns the arc tangent of one or two numbers
    String ATAN2 = "ATAN2";//Returns the arc tangent of two numbers
    String AVG = "AVG";//Returns the average value of an expression
    String CEIL = "CEIL";//Returns the smallest integer value that is >= to a number
    String CEILING = "CEILING";//Returns the smallest integer value that is >= to a number
    String COS = "COS";//Returns the cosine of a number
    String COT = "COT";//Returns the cotangent of a number
    String COUNT = "COUNT";//Returns the number of records returned by a select query
    String DEGREES = "DEGREES";//Converts a value in radians to degrees
    String DIV = "DIV";//	Used for integer division
    String EXP = "EXP";//Returns e raised to the power of a specified number
    String FLOOR = "FLOOR";//Returns the largest integer value that is <= to a number
    String GREATEST = "GREATEST";//Returns the greatest value of the list of arguments
    String LEAST = "LEAST";//Returns the smallest value of the list of arguments
    String LN = "LN";//Returns the natural logarithm of a number
    String LOG = "LOG";//Returns the natural logarithm of a number, or the logarithm of a number to a specified base
    String LOG10 = "LOG10";//Returns the natural logarithm of a number to base 10
    String LOG2 = "LOG2";//Returns the natural logarithm of a number to base 2
    String MAX = "MAX";//Returns the maximum value in a set of values
    String MIN = "MIN";//Returns the minimum value in a set of values
    String MOD = "MOD";//Returns the remainder of a number divided by another number
    String PI = "PI";//Returns the value of PI
    String POW = "POW";//Returns the value of a number raised to the power of another number
    String POWER = "POWER";//Returns the value of a number raised to the power of another number
    String RADIANS = "RADIANS";//Converts a degree value into radians
    String RAND = "RAND";//Returns a random number
    String ROUND = "ROUND";//	Rounds a number to a specified number of decimal places
    String SIGN = "SIGN";//Returns the sign of a number
    String SIN = "SIN";//Returns the sine of a number
    String SQRT = "SQRT";//Returns the square root of a number
    String SUM = "SUM";//	Calculates the sum of a set of values
    String TAN = "TAN";//Returns the tangent of a number
    String TRUNCATE = "TRUNCATE";//	Truncates a number to the specified number of decimal places

    //MySQL Date Functions
    String ADDDATE = "ADDDATE";//Adds a time/date interval to a date and then returns the date
    String ADDTIME = "ADDTIME";//Adds a time interval to a time/datetime and then returns the time/datetime
    String CURDATE = "CURDATE";//Returns the current date
    String CURRENT_DATE = "CURRENT_DATE";//Returns the current date
    String CURRENT_TIME = "CURRENT_TIME";//Returns the current time
    String CURRENT_TIMESTAMP = "CURRENT_TIMESTAMP";//Returns the current date and time
    String CURTIME = "CURTIME";//Returns the current time
    String DATE = "DATE";//Extracts the date part from a datetime expression
    String DATEDIFF = "DATEDIFF";//Returns the number of days between two date values
    String DATE_ADD = "DATE_ADD";//Adds a time/date interval to a date and then returns the date
    String DATE_FORMAT = "DATE_FORMAT";//Formats a date
    String DATE_SUB = "DATE_SUB";//Subtracts a time/date interval from a date and then returns the date
    String DAY = "DAY";//Returns the day of the month for a given date
    String DAYNAME = "DAYNAME";//Returns the weekday name for a given date
    String DAYOFMONTH = "DAYOFMONTH";//Returns the day of the month for a given date
    String DAYOFWEEK = "DAYOFWEEK";//Returns the weekday index for a given date
    String DAYOFYEAR = "DAYOFYEAR";//Returns the day of the year for a given date
    String EXTRACT = "EXTRACT";//Extracts a part from a given date
    String FROM_DAYS = "FROM_DAYS";//Returns a date from a numeric datevalue
    String HOUR = "HOUR";//Returns the hour part for a given date
    String LAST_DAY = "LAST_DAY";//Extracts the last day of the month for a given date
    String LOCALTIME = "LOCALTIME";//Returns the current date and time
    String LOCALTIMESTAMP = "LOCALTIMESTAMP";//Returns the current date and time
    String MAKEDATE = "MAKEDATE";//Creates and returns a date based on a year and a number of days value
    String MAKETIME = "MAKETIME";//Creates and returns a time based on an hour, minute, and second value
    String MICROSECOND = "MICROSECOND";//Returns the microsecond part of a time/datetime
    String MINUTE = "MINUTE";//Returns the minute part of a time/datetime
    String MONTH = "MONTH";//Returns the month part for a given date
    String MONTHNAME = "MONTHNAME";//Returns the name of the month for a given date
    String NOW = "NOW";//Returns the current date and time
    String PERIOD_ADD = "PERIOD_ADD";//Adds a specified number of months to a period
    String PERIOD_DIFF = "PERIOD_DIFF";//Returns the difference between two periods
    String QUARTER = "QUARTER";//Returns the quarter of the year for a given date value
    String SECOND = "SECOND";//Returns the seconds part of a time/datetime
    String SEC_TO_TIME = "SEC_TO_TIME";//Returns a time value based on the specified seconds
    String STR_TO_DATE = "STR_TO_DATE";//Returns a date based on a string and a format
    String SUBDATE = "SUBDATE";//Subtracts a time/date interval from a date and then returns the date
    String SUBTIME = "SUBTIME";//Subtracts a time interval from a datetime and then returns the time/datetime
    String SYSDATE = "SYSDATE";//Returns the current date and time
    String TIME = "TIME";//Extracts the time part from a given time/datetime
    String TIME_FORMAT = "TIME_FORMAT";//Formats a time by a specified format
    String TIME_TO_SEC = "TIME_TO_SEC";//Converts a time value into seconds
    String TIMEDIFF = "TIMEDIFF";//Returns the difference between two time/datetime expressions
    String TIMESTAMP = "TIMESTAMP";//Returns a datetime value based on a date or datetime value
    String TO_DAYS = "TO_DAYS";//Returns the number of days between a date and date "0000-00-00"
    String WEEK = "WEEK";//Returns the week number for a given date
    String WEEKDAY = "WEEKDAY";//Returns the weekday number for a given date
    String WEEKOFYEAR = "WEEKOFYEAR";//Returns the week number for a given date
    String YEAR = "YEAR";//Returns the year part for a given date
    String YEARWEEK = "YEARWEEK";//Returns the year and week number for a given date


    //MySQL Advanced Functions
    String BIN = "BIN";//Returns a binary representation of a number
    String BINARY = "BINARY";//Converts a value to a binary string
    String CASE = "CASE";//Goes through conditions and return a value when the first condition is met
    String CAST = "CAST";//Converts a value (of any type) into a specified datatype
    String COALESCE = "COALESCE";//Returns the first non-null value in a list
    String CONNECTION_ID = "CONNECTION_ID";//Returns the unique connection ID for the current connection
    String CONV = "CONV";//Converts a number from one numeric base system to another
    String CONVERT = "CONVERT";//Converts a value into the specified datatype or character set
    String CURRENT_USER = "CURRENT_USER";//Returns the user name and host name for the MySQL account that the server used to authenticate the current client
    String DATABASE = "DATABASE";//Returns the name of the current database
    String IF = "IF";//Returns a value if a condition is TRUE, or another value if a condition is FALSE
    String IFNULL = "IFNULL";//Return a specified value if the expression is NULL, otherwise return the expression
    String ISNULL = "ISNULL";//Returns 1 or 0 depending on whether an expression is NULL
    String LAST_INSERT_ID = "LAST_INSERT_ID";//Returns the AUTO_INCREMENT id of the last row that has been inserted or updated in a table
    String NULLIF = "NULLIF";//Compares two expressions and returns NULL if they are equal. Otherwise, the first expression is returned
    String SESSION_USER = "SESSION_USER";//Returns the current MySQL user name and host name
    String SYSTEM_USER = "SYSTEM_USER";//Returns the current MySQL user name and host name
    String USER = "USER";//Returns the current MySQL user name and host name
    String VERSION = "VERSION";//Returns the current version of the MySQL database
}
