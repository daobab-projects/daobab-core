package io.daobab.statement.function.dictionary;

@SuppressWarnings("unused")
public interface DictFunctionMicrosoftSql {

    String INTERVAL = "INTERVAL";
    String DISTINCT = "DISTINCT";

    //MsSQL String Functions
    //Returns the ASCII value for the specific character
    String ASCII = "ASCII";
    //Returns the character based on the ASCII code
    String CHAR = "CHAR";
    //Returns the position of a substring in a string
    String CHARINDEX = "CHARINDEX";
    //Adds two or more expressions together
    String CONCAT = "CONCAT";
    //Adds two or more expressions together with a separator
    String CONCAT_WS = "CONCAT_WS";
    //Formats a value with the specified format
    String FORMAT = "FORMAT";
    //Extracts a number of characters from a string (starting from left)
    String LEFT = "LEFT";
    //Returns the length of a string (in bytes)
    String LEN = "LEN";
    //Converts a string to lower-case
    String LOWER = "LOWER";
    //Removes leading spaces from a string
    String LTRIM = "LTRIM";
    //Returns the position of a pattern in a string
    String PATINDEX = "PATINDEX";
    //Replaces all occurrences of a substring within a string, with a new substring
    String REPLACE = "REPLACE";
    //Repeats a string a specified number of times
    String REPLICATE = "REPLICATE";
    //Reverses a string and returns the result
    String REVERSE = "REVERSE";
    //Extracts a number of characters from a string (starting from right)
    String RIGHT = "RIGHT";
    //Removes trailing spaces from a string
    String RTRIM = "RTRIM";
    //Returns a string of the specified number of space characters
    String SPACE = "SPACE";
    //Compares two strings
    String STRCMP = "STRCMP";
    //Returns a number as string
    String STR = "STR";
    //Deletes a part of a string and then inserts another part into the string, starting at a specified position
    String STUFF = "STUFF";
    //Extracts a substring from a string
    String SUBSTRING = "SUBSTRING";
    //Returns the string from the first argument after the characters specified in the second argument are translated into the characters specified in the third argument.
    String TRANSLATE = "TRANSLATE";
    //Removes leading and trailing spaces from a string
    String TRIM = "TRIM";
    //Converts a string to upper-case
    String UPPER = "UPPER";

    //    MsSQL Numeric Functions
    String ABS = "ABS";//Returns the absolute value of a number
    String ACOS = "ACOS";//Returns the arc cosine of a number
    String ASIN = "ASIN";//Returns the arc sine of a number
    String ATAN = "ATAN";//Returns the arc tangent of one or two numbers
    String ATAN2 = "ATAN2";//Returns the arc tangent of two numbers
    String AVG = "AVG";//Returns the average value of an expression
    String CEILING = "CEILING";//Returns the smallest integer value that is >= to a number
    String COS = "COS";//Returns the cosine of a number
    String COT = "COT";//Returns the cotangent of a number
    String COUNT = "COUNT";//Returns the number of records returned by a select query
    String DEGREES = "DEGREES";//Converts a value in radians to degrees
    String EXP = "EXP";//Returns e raised to the power of a specified number
    String LOG = "LOG";//Returns the natural logarithm of a number, or the logarithm of a number to a specified base
    String LOG10 = "LOG10";//Returns the natural logarithm of a number to base 10
    String MAX = "MAX";//Returns the maximum value in a set of values
    String MIN = "MIN";//Returns the minimum value in a set of values
    String PI = "PI";//Returns the value of PI
    String POWER = "POWER";//Returns the value of a number raised to the power of another number
    String RADIANS = "RADIANS";//Converts a degree value into radians
    String RAND = "RAND";//Returns a random number
    String ROUND = "ROUND";//	Rounds a number to a specified number of decimal places
    String SIGN = "SIGN";//Returns the sign of a number
    String SIN = "SIN";//Returns the sine of a number
    String SQRT = "SQRT";//Returns the square root of a number
    String SQUARE = "SQUARE";//	Returns the square of a number
    String SUM = "SUM";//	Calculates the sum of a set of values
    String TAN = "TAN";//Returns the tangent of a number

    //MsSQL Date Functions
    String CURRENT_TIMESTAMP = "CURRENT_TIMESTAMP";//Returns the current date and time
    String DATE_ADD = "DATE_ADD";//Adds a time/date interval to a date and then returns the date
    String DATEDIFF = "DATEDIFF";//Returns the difference between two dates
    String DATEFROMPARTS = "DATEFROMPARTS";//Returns a date from the specified parts (year, month, and day values)
    String DATENAME = "DATENAME";//	Returns a specified part of a date (as string)
    String DATEPART = "DATEPART";//	Returns a specified part of a date (as integer)
    String DAY = "DAY";//Returns the day of the month for a given date
    String GETDATE = "GETDATE";//Returns the current database system date and time
    String GETUTCDATE = "GETUTCDATE";//Returns the current database system UTC date and time
    String ISDATE = "ISDATE";//	Checks an expression and returns 1 if it is a valid date, otherwise 0
    String MONTH = "MONTH";//Returns the month part for a given date
    String SYSDATETIME = "SYSDATETIME";//Returns the date and time of the SQL Server
    String YEAR = "YEAR";//Returns the year part for a given date


    //MsSQL Advanced Functions
    String CAST = "CAST";//Converts a value (of any type) into a specified datatype
    String COALESCE = "COALESCE";//Returns the first non-null value in a list
    String CONVERT = "CONVERT";//Converts a value into the specified datatype or character set
    String CURRENT_USER = "CURRENT_USER";//Returns the user name and host name for the MsSQL account that the server used to authenticate the current client
    String IIF = "IIF";//Returns a value if a condition is TRUE, or another value if a condition is FALSE
    String ISNULL = "ISNULL";//Returns 1 or 0 depending on whether an expression is NULL
    String ISNUMERIC = "ISNUMERIC";//Tests whether an expression is numeric
    String NULLIF = "NULLIF";//Returns NULL if two expressions are equal
    String SESSION_USER = "SESSION_USER";//Returns the name of the current user in the SQL Server database
    String SESSIONPROPERTY = "SESSIONPROPERTY";//Returns the session settings for a specified option
    String SYSTEM_USER = "SYSTEM_USER";//	Returns the login name for the current user
    String USER_NAME = "USER_NAME";//	Returns the database user name based on the specified id
}
