package io.daobab.statement.function;

import io.daobab.error.MandatoryFunctionParameter;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;
import io.daobab.query.marker.ColumnOrQuery;
import io.daobab.statement.function.base.DatePeriod;
import io.daobab.statement.function.base.FunctionKey;
import io.daobab.statement.function.dictionary.DictFunctionOracle;
import io.daobab.statement.function.type.ColumnFunction;
import io.daobab.statement.function.type.NoParamFunction;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings({"rawtypes", "unused"})
public interface FunctionWhispererOracle {


    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> ascii(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.ASCII, Integer.class);
    }

    /**
     * CHR returns the character having the binary equivalent to n as a VARCHAR2 value in either the database character set or, if you specify USING NCHAR_CS, the national character set.
     * <p>
     * For single-byte character sets, if n > 256, then Oracle Database returns the binary equivalent of n mod 256. For multibyte character sets, n must resolve to one entire code point. Invalid code points are not validated, and the result of specifying invalid code points is indeterminate.
     * <p>
     * This function takes as an argument a NUMBER value, or any value that can be implicitly converted to NUMBER, and returns a character.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> chr(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.CHR, String.class);
    }

    /**
     * CONCAT returns char1 concatenated with char2. Both char1 and char2 can be any of the datatypes CHAR, VARCHAR2, NCHAR, NVARCHAR2, CLOB, or NCLOB. The string returned is in the same character set as char1. Its datatype depends on the datatypes of the arguments.
     * <p>
     * In concatenations of two different datatypes, Oracle Database returns the datatype that results in a lossless conversion. Therefore, if one of the arguments is a LOB, then the returned value is a LOB. If one of the arguments is a national datatype, then the returned value is a national datatype. For example:
     * <p>
     * CONCAT(CLOB, NCLOB) returns NCLOB
     * <p>
     * CONCAT(NCLOB, NCHAR) returns NCLOB
     * <p>
     * CONCAT(NCLOB, CHAR) returns NCLOB
     * <p>
     * CONCAT(NCHAR, CLOB) returns NCLOB
     * <p>
     * This function is equivalent to the concatenation operator (||).
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> concat(ColumnOrQuery<E, F, R> column1, ColumnOrQuery<?, ?, ?> column2) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(column1, DictFunctionOracle.CONCAT, String.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, column2);
        return rv;
    }

    /**
     * CONCAT returns char1 concatenated with char2. Both char1 and char2 can be any of the datatypes CHAR, VARCHAR2, NCHAR, NVARCHAR2, CLOB, or NCLOB. The string returned is in the same character set as char1. Its datatype depends on the datatypes of the arguments.
     * <p>
     * In concatenations of two different datatypes, Oracle Database returns the datatype that results in a lossless conversion. Therefore, if one of the arguments is a LOB, then the returned value is a LOB. If one of the arguments is a national datatype, then the returned value is a national datatype. For example:
     * <p>
     * CONCAT(CLOB, NCLOB) returns NCLOB
     * <p>
     * CONCAT(NCLOB, NCHAR) returns NCLOB
     * <p>
     * CONCAT(NCLOB, CHAR) returns NCLOB
     * <p>
     * CONCAT(NCHAR, CLOB) returns NCLOB
     * <p>
     * This function is equivalent to the concatenation operator (||).
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> concat(ColumnOrQuery<E, F, R> column, String str) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(column, DictFunctionOracle.CONCAT, String.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, str);
        return rv;
    }

    /**
     * CONCAT returns char1 concatenated with char2. Both char1 and char2 can be any of the datatypes CHAR, VARCHAR2, NCHAR, NVARCHAR2, CLOB, or NCLOB. The string returned is in the same character set as char1. Its datatype depends on the datatypes of the arguments.
     * <p>
     * In concatenations of two different datatypes, Oracle Database returns the datatype that results in a lossless conversion. Therefore, if one of the arguments is a LOB, then the returned value is a LOB. If one of the arguments is a national datatype, then the returned value is a national datatype. For example:
     * <p>
     * CONCAT(CLOB, NCLOB) returns NCLOB
     * CONCAT(NCLOB, NCHAR) returns NCLOB
     * CONCAT(NCLOB, CHAR) returns NCLOB
     * CONCAT(NCHAR, CLOB) returns NCLOB
     * <p>
     * This function is equivalent to the concatenation operator (||).
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> concat(String str, ColumnOrQuery<E, F, R> column) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(column, DictFunctionOracle.CONCAT, String.class);
        rv.setKeyValue(ColumnFunction.BEFORE_COL, str);
        return rv;
    }

    /**
     * INITCAP returns char, with the first letter of each word in uppercase, all other letters in lowercase. Words are delimited by white space or characters that are not alphanumeric.
     * char can be of any of the datatypes CHAR, VARCHAR2, NCHAR, or NVARCHAR2. The return value is the same datatype as char. The database sets the case of the initial characters based on the binary mapping defined for the underlying character set. For linguistic-sensitive uppercase and lowercase, please refer to NLS_INITCAP.
     * This function does not support CLOB data directly. However, CLOBs can be passed in as arguments through implicit data conversion.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> initcap(String... values) {
        return new ColumnFunction<>(DictFunctionOracle.INITCAP, String.class, values);
    }

    /**
     *
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> instr(String... values) {
        return new ColumnFunction<>(DictFunctionOracle.INSTR, Integer.class, values);
    }

    /**
     *
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> length(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.LENGTH, Integer.class);
    }

    /**
     * LOWER returns char, with all letters lowercase. char can be any of the datatypes CHAR, VARCHAR2, NCHAR, NVARCHAR2, CLOB, or NCLOB. The return value is the same datatype as char. The database sets the case of the characters based on the binary mapping defined for the underlying character set. For linguistic-sensitive lowercase, please refer to NLS_LOWER.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> lower(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.LOWER, String.class);
    }

    /**
     * LPAD returns expr1, left-padded to length n characters with the sequence of characters in expr2. This function is useful for formatting the output of a query.
     * Both expr1 and expr2 can be any of the datatypes CHAR, VARCHAR2, NCHAR, NVARCHAR2, CLOB, or NCLOB. The string returned is of VARCHAR2 datatype if expr1 is a character datatype and a LOB if expr1 is a LOB datatype. The string returned is in the same character set as expr1. The argument n must be a NUMBER integer or a value that can be implicitly converted to a NUMBER integer.
     * If you do not specify expr2, then the default is a single blank. If expr1 is longer than n, then this function returns the portion of expr1 that fits in n.
     * The argument n is the total length of the return value as it is displayed on your terminal screen. In most character sets, this is also the number of characters in the return value. However, in some multibyte character sets, the display length of a character string can differ from the number of characters in the string.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> lpad(ColumnOrQuery<E, F, R> column, int n, String expr) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(column, DictFunctionOracle.LPAD, String.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, n);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, expr);
        return rv;
    }

    /**
     * LTRIM removes from the left end of char all of the characters contained in set. If you do not specify set, it defaults to a single blank. If char is a character literal, then you must enclose it in single quotes. Oracle Database begins scanning char from its first character and removes all characters that appear in set until reaching a character not in set and then returns the result.
     * Both char and set can be any of the datatypes CHAR, VARCHAR2, NCHAR, NVARCHAR2, CLOB, or NCLOB. The string returned is of VARCHAR2 datatype if char is a character datatype and a LOB if char is a LOB datatype.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> ltrim(ColumnOrQuery<E, F, R> column, String str) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(column, DictFunctionOracle.LTRIM, String.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, str);
        return rv;
    }

    /**
     * LTRIM removes from the left end of char all of the characters contained in set. If you do not specify set, it defaults to a single blank. If char is a character literal, then you must enclose it in single quotes. Oracle Database begins scanning char from its first character and removes all characters that appear in set until reaching a character not in set and then returns the result.
     * Both char and set can be any of the datatypes CHAR, VARCHAR2, NCHAR, NVARCHAR2, CLOB, or NCLOB. The string returned is of VARCHAR2 datatype if char is a character datatype and a LOB if char is a LOB datatype.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> ltrim(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.LTRIM, String.class);
    }

    /**
     * NLS_INITCAP returns char, with the first letter of each word in uppercase, all other letters in lowercase. Words are delimited by white space or characters that are not alphanumeric.
     * <p>
     * Both char and 'nlsparam' can be any of the datatypes CHAR, VARCHAR2, NCHAR, or NVARCHAR2. The string returned is of VARCHAR2 datatype and is in the same character set as char.
     * <p>
     * The value of 'nlsparam' can have this form:
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> nlsInitcap(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.NLS_INITCAP);
    }

    /**
     * NLS_LOWER returns char, with all letters lowercase.
     * <p>
     * Both char and 'nlsparam' can be any of the datatypes CHAR, VARCHAR2, NCHAR, NVARCHAR2, CLOB, or NCLOB. The string returned is of VARCHAR2 datatype if char is a character datatype and a LOB if char is a LOB datatype. The return string is in the same character set as char.
     * <p>
     * The 'nlsparam' can have the same form and serve the same purpose as in the NLS_INITCAP function.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> nlsLower(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.NLS_LOWER);
    }

    /**
     * NLSSORT returns the string of bytes used to sort char.
     * <p>
     * Both char and 'nlsparam' can be any of the datatypes CHAR, VARCHAR2, NCHAR, or NVARCHAR2. The string returned is of RAW datatype.
     * <p>
     * The value of 'nlsparam' can have the form
     * <p>
     * 'NLS_SORT = sort'
     * <p>
     * where sort is a linguistic sort sequence or BINARY. If you omit 'nlsparam', then this function uses the default sort sequence for your session. If you specify BINARY, then this function returns char.
     * <p>
     * If you specify 'nlsparam', then you can append to the linguistic sort name the suffix _ai to request an accent-insensitive sort or _ci to request a case-insensitive sort. Please refer to Oracle Database Globalization Support Guide for more information on accent- and case-insensitive sorting.
     * <p>
     * This function does not support CLOB data directly. However, CLOBs can be passed in as arguments through implicit data conversion.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> nlssort(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.NLSSORT);
    }

    /**
     * NLS_UPPER returns char, with all letters uppercase.
     * <p>
     * Both char and 'nlsparam' can be any of the datatypes CHAR, VARCHAR2, NCHAR, NVARCHAR2, CLOB, or NCLOB. The string returned is of VARCHAR2 datatype if char is a character datatype and a LOB if char is a LOB datatype. The return string is in the same character set as char.
     * <p>
     * The 'nlsparam' can have the same form and serve the same purpose as in the NLS_INITCAP function.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> nlsUpper(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.NLS_UPPER);
    }

    /**
     * REGEXP_REPLACE extends the functionality of the REPLACE function by letting you search a string for a regular expression pattern. By default, the function returns source_char with every occurrence of the regular expression pattern replaced with replace_string. The string returned is in the same character set as source_char. The function returns VARCHAR2 if the first argument is not a LOB and returns CLOB if the first argument is a LOB.
     * This function complies with the POSIX regular expression standard and the Unicode Regular Expression Guidelines. For more information, please refer to Appendix C, "Oracle Regular Expression Support".
     * source_char is a character expression that serves as the search value. It is commonly a character column and can be of any of the datatypes CHAR, VARCHAR2, NCHAR, NVARCHAR2, CLOB or NCLOB.
     * pattern is the regular expression. It is usually a text literal and can be of any of the datatypes CHAR, VARCHAR2, NCHAR, or NVARCHAR2. It can contain up to 512 bytes. If the datatype of pattern is different from the datatype of source_char, Oracle Database converts pattern to the datatype of source_char. For a listing of the operators you can specify in pattern, please refer to Appendix C, "Oracle Regular Expression Support".
     * replace_string can be of any of the datatypes CHAR, VARCHAR2, NCHAR, NVARCHAR2, CLOB, or NCLOB. If replace_string is a CLOB or NCLOB, then Oracle truncates replace_string to 32K. The replace_string can contain up to 500 backreferences to subexpressions in the form \n, where n is a number from 1 to 9. If n is the backslash character in replace_string, then you must precede it with the escape character (\\). For more information on backreference expressions, please refer to the notes to "Oracle Regular Expression Support", Table C-1.
     * position is a positive integer indicating the character of source_char where Oracle should begin the search. The default is 1, meaning that Oracle begins the search at the first character of source_char.
     * occurrence is a nonnegative integer indicating the occurrence of the replace operation:
     * If you specify 0, then Oracle replaces all occurrences of the match.
     * If you specify a positive integer n, then Oracle replaces the nth occurrence.
     * match_parameter is a text literal that lets you change the default matching behavior of the function. This argument affects only the matching process and has no effect on replace_string. You can specify one or more of the following values for match_parameter:
     * <p>
     * 'i' specifies case-insensitive matching.
     * 'c' specifies case-sensitive matching.
     * 'n' allows the period (.), which is the match-any-character character, to match the newline character. If you omit this parameter, the period does not match the newline character.
     * 'm' treats the source string as multiple lines. Oracle interprets ^ and $ as the start and end, respectively, of any line anywhere in the source string, rather than only at the start or end of the entire source string. If you omit this parameter, Oracle treats the source string as a single line.
     * 'x' ignores whitespace characters. By default, whitespace characters match themselves.
     * <p>
     * If you specify multiple contradictory values, Oracle uses the last value. For example, if you specify 'ic', then Oracle uses case-sensitive matching. If you specify a character other than those shown above, then Oracle returns an error.
     * If you omit match_parameter, then:
     * The default case sensitivity is determined by the value of the NLS_SORT parameter.
     * A period (.) does not match the newline character.
     * The source string is treated as a single line.
     */
    default <E extends Entity, R extends EntityRelation> ColumnFunction<E, String, R, String> regexpReplace(ColumnOrQuery<E, String, R> column, String regex, String replace) {
        ColumnFunction<E, String, R, String> rv = new ColumnFunction<>(column, DictFunctionOracle.REGEXP_REPLACE);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, regex);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, replace);
        return rv;
    }

    /**
     * REGEXP_SUBSTR extends the functionality of the SUBSTR function by letting you search a string for a regular expression pattern. It is also similar to REGEXP_INSTR, but instead of returning the position of the substring, it returns the substring itself. This function is useful if you need the contents of a match string but not its position in the source string. The function returns the string as VARCHAR2 or CLOB data in the same character set as source_char.
     * <p>
     * This function complies with the POSIX regular expression standard and the Unicode Regular Expression Guidelines. For more information, please refer to Appendix C, "Oracle Regular Expression Support".
     * <p>
     * source_char is a character expression that serves as the search value. It is commonly a character column and can be of any of the datatypes CHAR, VARCHAR2, NCHAR, NVARCHAR2, CLOB, or NCLOB.
     * <p>
     * pattern is the regular expression. It is usually a text literal and can be of any of the datatypes CHAR, VARCHAR2, NCHAR, or NVARCHAR2. It can contain up to 512 bytes. If the datatype of pattern is different from the datatype of source_char, Oracle Database converts pattern to the datatype of source_char. For a listing of the operators you can specify in pattern, please refer to Appendix C, "Oracle Regular Expression Support".
     * <p>
     * position is a positive integer indicating the character of source_char where Oracle should begin the search. The default is 1, meaning that Oracle begins the search at the first character of source_char.
     * <p>
     * occurrence is a positive integer indicating which occurrence of pattern in source_char Oracle should search for. The default is 1, meaning that Oracle searches for the first occurrence of pattern.
     * <p>
     * match_parameter is a text literal that lets you change the default matching behavior of the function. You can specify one or more of the following values for match_parameter:
     * <p>
     * 'i' specifies case-insensitive matching.
     * <p>
     * 'c' specifies case-sensitive matching.
     * <p>
     * 'n' allows the period (.), which is the match-any-character character, to match the newline character. If you omit this parameter, the period does not match the newline character.
     * <p>
     * 'm' treats the source string as multiple lines. Oracle interprets ^ and $ as the start and end, respectively, of any line anywhere in the source string, rather than only at the start or end of the entire source string. If you omit this parameter, Oracle treats the source string as a single line.
     * <p>
     * 'x' ignores whitespace characters. By default, whitespace characters match themselves.
     * <p>
     * If you specify multiple contradictory values, Oracle uses the last value. For example, if you specify 'ic', then Oracle uses case-sensitive matching. If you specify a character other than those shown above, then Oracle returns an error.
     * <p>
     * If you omit match_parameter, then:
     * <p>
     * The default case sensitivity is determined by the value of the NLS_SORT parameter.
     * <p>
     * A period (.) does not match the newline character.
     * <p>
     * The source string is treated as a single line.
     */
    default <E extends Entity, R extends EntityRelation> ColumnFunction<E, String, R, String> regexpSubstr(ColumnOrQuery<E, String, R> column, String regex, String replace) {
        ColumnFunction<E, String, R, String> rv = new ColumnFunction<>(column, DictFunctionOracle.REGEXP_SUBSTR);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, regex);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, replace);
        return rv;
    }

    /**
     * REPLACE returns char with every occurrence of search_string replaced with replacement_string. If replacement_string is omitted or null, then all occurrences of search_string are removed. If search_string is null, then char is returned.
     * <p>
     * Both search_string and replacement_string, as well as char, can be any of the datatypes CHAR, VARCHAR2, NCHAR, NVARCHAR2, CLOB, or NCLOB. The string returned is in the same character set as char. The function returns VARCHAR2 if the first argument is not a LOB and returns CLOB if the first argument is a LOB.
     * <p>
     * REPLACE provides functionality related to that provided by the TRANSLATE function. TRANSLATE provides single-character, one-to-one substitution. REPLACE lets you substitute one string for another as well as to remove character strings.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> replace(ColumnOrQuery<E, F, R> column, String searchString, String replacement) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(column, DictFunctionOracle.REPLACE, String.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, searchString);
        rv.setKeyValue(ColumnFunction.AFTER_COL2, replacement);
        return rv;
    }

    /**
     * RPAD returns expr1, right-padded to length n characters with expr2, replicated as many times as necessary. This function is useful for formatting the output of a query.
     * <p>
     * Both expr1 and expr2 can be any of the datatypes CHAR, VARCHAR2, NCHAR, NVARCHAR2, CLOB, or NCLOB. The string returned is of VARCHAR2 datatype if expr1 is a character datatype and a LOB if expr1 is a LOB datatype. The string returned is in the same character set as expr1. The argument n must be a NUMBER integer or a value that can be implicitly converted to a NUMBER integer.
     * <p>
     * expr1 cannot be null. If you do not specify expr2, then it defaults to a single blank. If expr1 is longer than n, then this function returns the portion of expr1 that fits in n.
     * <p>
     * The argument n is the total length of the return value as it is displayed on your terminal screen. In most character sets, this is also the number of characters in the return value. However, in some multibyte character sets, the display length of a character string can differ from the number of characters in the string.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> rpad(ColumnOrQuery<E, F, R> column, String expr1, String expr2) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(column, DictFunctionOracle.RPAD, String.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, expr1);
        rv.setKeyValue(ColumnFunction.AFTER_COL2, expr2);
        return rv;
    }

    /**
     * RTRIM removes from the right end of char all of the characters that appear in set. This function is useful for formatting the output of a query.
     * If you do not specify set, then it defaults to a single blank. If char is a character literal, then you must enclose it in single quotes. RTRIM works similarly to LTRIM.
     * Both char and set can be any of the datatypes CHAR, VARCHAR2, NCHAR, NVARCHAR2, CLOB, or NCLOB. The string returned is of VARCHAR2 datatype if char is a character datatype and a LOB if char is a LOB datatype.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> rtrim(ColumnOrQuery<E, F, R> column, String expr1) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(column, DictFunctionOracle.RTRIM, String.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, expr1);
        return rv;
    }

    /**
     * SOUNDEX returns a character string containing the phonetic representation of char. This function lets you compare words that are spelled differently, but sound alike in English.
     * <p>
     * The phonetic representation is defined in The Art of Computer Programming, Volume 3: Sorting and Searching, by Donald E. Knuth, as follows:
     * <p>
     * Retain the first letter of the string and remove all other occurrences of the following letters: a, e, h, i, o, u, w, y.
     * <p>
     * Assign numbers to the remaining letters (after the first) as follows:
     * <p>
     * b, f, p, v = 1
     * c, g, j, k, q, s, x, z = 2
     * d, t = 3
     * l = 4
     * m, n = 5
     * r = 6
     * <p>
     * If two or more letters with the same number were adjacent in the original name (before step 1), or adjacent except for any intervening h and w, then omit all but the first.
     * <p>
     * Return the first four bytes padded with 0.
     * <p>
     * char can be of any of the datatypes CHAR, VARCHAR2, NCHAR, or NVARCHAR2. The return value is the same datatype as char.
     * <p>
     * This function does not support CLOB data directly. However, CLOBs can be passed in as arguments through implicit data conversion.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> soundex(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.SOUNDEX, String.class);
    }

    /**
     * SOUNDEX returns a character string containing the phonetic representation of char. This function lets you compare words that are spelled differently, but sound alike in English.
     * <p>
     * The phonetic representation is defined in The Art of Computer Programming, Volume 3: Sorting and Searching, by Donald E. Knuth, as follows:
     * <p>
     * Retain the first letter of the string and remove all other occurrences of the following letters: a, e, h, i, o, u, w, y.
     * <p>
     * Assign numbers to the remaining letters (after the first) as follows:
     * <p>
     * b, f, p, v = 1
     * c, g, j, k, q, s, x, z = 2
     * d, t = 3
     * l = 4
     * m, n = 5
     * r = 6
     * <p>
     * If two or more letters with the same number were adjacent in the original name (before step 1), or adjacent except for any intervening h and w, then omit all but the first.
     * <p>
     * Return the first four bytes padded with 0.
     * <p>
     * char can be of any of the datatypes CHAR, VARCHAR2, NCHAR, or NVARCHAR2. The return value is the same datatype as char.
     * <p>
     * This function does not support CLOB data directly. However, CLOBs can be passed in as arguments through implicit data conversion.
     */
    //TODO:Check
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> soundex(String str) {
        return new ColumnFunction<>(str, DictFunctionOracle.SOUNDEX, String.class);
    }

    /**
     * The SUBSTR functions return a portion of char, beginning at character position, substring_length characters long. SUBSTR calculates lengths using characters as defined by the input character set. SUBSTRB uses bytes instead of characters. SUBSTRC uses Unicode complete characters. SUBSTR2 uses UCS2 code points. SUBSTR4 uses UCS4 code points.
     * <p>
     * If position is 0, then it is treated as 1.
     * <p>
     * If position is positive, then Oracle Database counts from the beginning of char to find the first character.
     * <p>
     * If position is negative, then Oracle counts backward from the end of char.
     * <p>
     * If substring_length is omitted, then Oracle returns all characters to the end of char. If substring_length is less than 1, then Oracle returns null.
     * <p>
     * char can be any of the datatypes CHAR, VARCHAR2, NCHAR, NVARCHAR2, CLOB, or NCLOB. Both position and substring_length must be of datatype NUMBER, or any datatype that can be implicitly converted to NUMBER, and must resolve to an integer. The return value is the same datatype as char. Floating-point numbers passed as arguments to SUBSTR are automatically converted to integers.
     * <p>
     * See Also:
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> substr(ColumnOrQuery<E, F, R> column, int start, int length) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(column, DictFunctionOracle.SUBSTR, String.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, start);
        rv.setKeyValue(ColumnFunction.AFTER_COL2, length);
        return rv;
    }

    /**
     * TRANSLATE returns expr with all occurrences of each character in from_string replaced by its corresponding character in to_string. Characters in expr that are not in from_string are not replaced. If expr is a character string, then you must enclose it in single quotation marks. The argument from_string can contain more characters than to_string. In this case, the extra characters at the end of from_string have no corresponding characters in to_string. If these extra characters appear in char, then they are removed from the return value.
     * <p>
     * You cannot use an empty string for to_string to remove all characters in from_string from the return value. Oracle Database interprets the empty string as null, and if this function has a null argument, then it returns null.
     * <p>
     * TRANSLATE provides functionality related to that provided by the REPLACE function. REPLACE lets you substitute a single string for another single string, as well as remove character strings. TRANSLATE lets you make several single-character, one-to-one substitutions in one operation.
     * <p>
     * This function does not support CLOB data directly. However, CLOBs can be passed in as arguments through implicit data conversion.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> translate(ColumnOrQuery<E, F, R> column, String fromString, String toString) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(column, DictFunctionOracle.TRANSLATE, String.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, fromString);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, toString);
        return rv;
    }

    /**
     * TREAT changes the declared type of an expression.
     * <p>
     * You must have the EXECUTE object privilege on type to use this function.
     * <p>
     * type must be some supertype or subtype of the declared type of expr. If the most specific type of expr is type (or some subtype of type), then TREAT returns expr. If the most specific type of expr is not type (or some subtype of type), then TREAT returns NULL.
     * <p>
     * You can specify REF only if the declared type of expr is a REF type.
     * <p>
     * If the declared type of expr is a REF to a source type of expr, then type must be some subtype or supertype of the source type of expr. If the most specific type of DEREF(expr) is type (or a subtype of type), then TREAT returns expr. If the most specific type of DEREF(expr) is not type (or a subtype of type), then TREAT returns NULL.
     * <p>
     * This function does not support CLOB data directly. However, CLOBs can be passed in as arguments through implicit data conversion.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> treat(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.TREAT, String.class);
    }

    /**
     * TRIM enables you to trim leading or trailing characters (or both) from a character string. If trim_character or trim_source is a character literal, then you must enclose it in single quotes.
     * <p>
     * If you specify LEADING, then Oracle Database removes any leading characters equal to trim_character.
     * <p>
     * If you specify TRAILING, then Oracle removes any trailing characters equal to trim_character.
     * <p>
     * If you specify BOTH or none of the three, then Oracle removes leading and trailing characters equal to trim_character.
     * <p>
     * If you do not specify trim_character, then the default value is a blank space.
     * <p>
     * If you specify only trim_source, then Oracle removes leading and trailing blank spaces.
     * <p>
     * The function returns a value with datatype VARCHAR2. The maximum length of the value is the length of trim_source.
     * <p>
     * If either trim_source or trim_character is null, then the TRIM function returns null.
     * <p>
     * Both trim_character and trim_source can be any of the datatypes CHAR, VARCHAR2, NCHAR, NVARCHAR2, CLOB, or NCLOB. The string returned is of VARCHAR2 datatype if trim_source is a character datatype and a LOB if trim_source is a LOB datatype. The return string is in the same character set as trim_source.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> trim(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.TRIM, String.class);
    }

    /**
     * UPPER returns char, with all letters uppercase. char can be any of the datatypes CHAR, VARCHAR2, NCHAR, NVARCHAR2, CLOB, or NCLOB. The return value is the same datatype as char. The database sets the case of the characters based on the binary mapping defined for the underlying character set. For linguistic-sensitive uppercase, please refer to NLS_UPPER.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> upper(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.UPPER, String.class);
    }


    /**
     * ABS returns the absolute value of n.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> abs(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.ABS, BigDecimal.class);
    }

    /**
     * ACOS returns the arc cosine of n. The argument n must be in the range of -1 to 1, and the function returns a value in the range of 0 to pi, expressed in radians.
     * <p>
     * This function takes as an argument any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. If the argument is BINARY_FLOAT, then the function returns BINARY_DOUBLE. Otherwise the function returns the same numeric datatype as the argument.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> acos(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.ACOS, BigDecimal.class);
    }

    /**
     * ASIN returns the arc sine of n. The argument n must be in the range of -1 to 1, and the function returns a value in the range of -pi/2 to pi/2, expressed in radians.
     * <p>
     * This function takes as an argument any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. If the argument is BINARY_FLOAT, then the function returns BINARY_DOUBLE. Otherwise the function returns the same numeric datatype as the argument.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> asin(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.ASIN, BigDecimal.class);
    }

    /**
     * ATAN returns the arc tangent of n. The argument n can be in an unbounded range and returns a value in the range of -pi/2 to pi/2, expressed in radians.
     * <p>
     * This function takes as an argument any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. If the argument is BINARY_FLOAT, then the function returns BINARY_DOUBLE. Otherwise the function returns the same numeric datatype as the argument.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> atan(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.ATAN, BigDecimal.class);
    }

    /**
     * ATAN returns the arc tangent of n. The argument n can be in an unbounded range and returns a value in the range of -pi/2 to pi/2, expressed in radians.
     * <p>
     * This function takes as an argument any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. If the argument is BINARY_FLOAT, then the function returns BINARY_DOUBLE. Otherwise the function returns the same numeric datatype as the argument.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> atan(ColumnOrQuery<E, F, R> column, ColumnOrQuery<E, F, R> column2) {
        ColumnFunction<E, F, R, BigDecimal> rv = new ColumnFunction<>(column, DictFunctionOracle.ATAN, BigDecimal.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, column2);
        return rv;
    }

    /**
     * ATAN2 returns the arc tangent of n1 and n2. The argument n1 can be in an unbounded range and returns a value in the range of -pi to pi, depending on the signs of n1 and n2, expressed in radians. ATAN2(n1,n2) is the same as ATAN2(n1/n2).
     * <p>
     * This function takes as arguments any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. If any argument is BINARY_FLOAT or BINARY_DOUBLE, then the function returns BINARY_DOUBLE. Otherwise the function returns NUMBER.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> atan2(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.ATAN2, BigDecimal.class);
    }

    /**
     * ATAN2 returns the arc tangent of n1 and n2. The argument n1 can be in an unbounded range and returns a value in the range of -pi to pi, depending on the signs of n1 and n2, expressed in radians. ATAN2(n1,n2) is the same as ATAN2(n1/n2).
     * <p>
     * This function takes as arguments any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. If any argument is BINARY_FLOAT or BINARY_DOUBLE, then the function returns BINARY_DOUBLE. Otherwise the function returns NUMBER.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> atan2(ColumnOrQuery<E, F, R> column, ColumnOrQuery<E, F, R> column2) {
        ColumnFunction<E, F, R, BigDecimal> rv = new ColumnFunction<>(column, DictFunctionOracle.ATAN2, BigDecimal.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, column2);
        return rv;
    }

    /**
     * BITAND computes an AND operation on the bits of expr1 and expr2, both of which must resolve to nonnegative integers, and returns an integer. This function is commonly used with the DECODE function, as illustrated in the example that follows.
     * <p>
     * An AND operation compares two bit values. If the values are the same, the operator returns 1. If the values are different, the operator returns 0. Only significant bits are compared. For example, an AND operation on the integers 5 (binary 101) and 1 (binary 001 or 1) compares only the rightmost bit, and results in a value of 1 (binary 1).
     * <p>
     * Both arguments can be any numeric datatype, or any nonnumeric datatype that can be implicitly converted to NUMBER. The function returns NUMBER.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> bitand(ColumnOrQuery<E, F, R> column, ColumnOrQuery<E, F, R> column2) {
        ColumnFunction<E, F, R, BigDecimal> rv = new ColumnFunction<>(column, DictFunctionOracle.BITAND, BigDecimal.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, column2);
        return rv;
    }


    /**
     * BITAND computes an AND operation on the bits of expr1 and expr2, both of which must resolve to nonnegative integers, and returns an integer. This function is commonly used with the DECODE function, as illustrated in the example that follows.
     * <p>
     * An AND operation compares two bit values. If the values are the same, the operator returns 1. If the values are different, the operator returns 0. Only significant bits are compared. For example, an AND operation on the integers 5 (binary 101) and 1 (binary 001 or 1) compares only the rightmost bit, and results in a value of 1 (binary 1).
     * <p>
     * Both arguments can be any numeric datatype, or any nonnumeric datatype that can be implicitly converted to NUMBER. The function returns NUMBER.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> bitand(ColumnOrQuery<E, F, R> column, Number column2) {
        ColumnFunction<E, F, R, BigDecimal> rv = new ColumnFunction<>(column, DictFunctionOracle.BITAND, BigDecimal.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, column2);
        return rv;
    }

    /**
     * BITAND computes an AND operation on the bits of expr1 and expr2, both of which must resolve to nonnegative integers, and returns an integer. This function is commonly used with the DECODE function, as illustrated in the example that follows.
     * <p>
     * An AND operation compares two bit values. If the values are the same, the operator returns 1. If the values are different, the operator returns 0. Only significant bits are compared. For example, an AND operation on the integers 5 (binary 101) and 1 (binary 001 or 1) compares only the rightmost bit, and results in a value of 1 (binary 1).
     * <p>
     * Both arguments can be any numeric datatype, or any nonnumeric datatype that can be implicitly converted to NUMBER. The function returns NUMBER.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> bitand(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.BITAND, BigDecimal.class);
    }

//    /**
//     *
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> avg(ColumnOrQuery<E, F, R> column) {
//        return new ColumnFunction<>(column, DictFunctionOracle.AVG);
//    }

    /**
     * CEIL returns smallest integer greater than or equal to n.
     * <p>
     * This function takes as an argument any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. The function returns the same datatype as the numeric datatype of the argument.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> ceil(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.CEIL, BigDecimal.class);
    }

    /**
     * COS returns the cosine of n (an angle expressed in radians).
     * <p>
     * This function takes as an argument any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. If the argument is BINARY_FLOAT, then the function returns BINARY_DOUBLE. Otherwise the function returns the same numeric datatype as the argument.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> cos(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.COS, BigDecimal.class);
    }

    /**
     * COSH returns the hyperbolic cosine of n.
     * <p>
     * This function takes as an argument any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. If the argument is BINARY_FLOAT, then the function returns BINARY_DOUBLE. Otherwise the function returns the same numeric datatype as the argument.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> cosh(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.COSH, BigDecimal.class);
    }

//    /**
//     *
//     */
//    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> count(ColumnOrQuery<E, F, R> column) {
//        return new ColumnFunction<>(column, DictFunctionOracle.COUNT);
//    }

    /**
     * EXP returns e raised to the nth power, where e = 2.71828183 ... The function returns a value of the same type as the argument.
     * <p>
     * This function takes as an argument any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. If the argument is BINARY_FLOAT, then the function returns BINARY_DOUBLE. Otherwise the function returns the same numeric datatype as the argument.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> exp(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.EXP, BigDecimal.class);
    }

    /**
     * FLOOR returns largest integer equal to or less than n.
     * <p>
     * This function takes as an argument any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. The function returns the same datatype as the numeric datatype of the argument.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> floor(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.FLOOR, BigDecimal.class);
    }

    /**
     *
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> greatest(Column<?, ?, ?>... columns) {
        return new ColumnFunction<>(DictFunctionOracle.GREATEST, BigDecimal.class, columns);
    }

    /**
     *
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> least(Column<?, ?, ?>... columns) {
        return new ColumnFunction<>(DictFunctionOracle.LEAST, BigDecimal.class, columns);
    }

    /**
     * LN returns the natural logarithm of n, where n is greater than 0.
     * <p>
     * This function takes as an argument any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. If the argument is BINARY_FLOAT, then the function returns BINARY_DOUBLE. Otherwise the function returns the same numeric datatype as the argument.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> ln(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.LN, BigDecimal.class);
    }

    /**
     * LOG returns the logarithm, base n2, of n1. The base n1 can be any positive value other than 0 or 1 and n2 can be any positive value.
     * <p>
     * This function takes as arguments any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. If any argument is BINARY_FLOAT or BINARY_DOUBLE, then the function returns BINARY_DOUBLE. Otherwise the function returns NUMBER.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> log(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.LOG, BigDecimal.class);
    }

    /**
     * MOD returns the remainder of n2 divided by n1. Returns n2 if n1 is 0.
     * <p>
     * This function takes as arguments any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. Oracle determines the argument with the highest numeric precedence, implicitly converts the remaining arguments to that datatype, and returns that datatype.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> mod(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.MOD, BigDecimal.class);
    }

    /**
     * The NANVL function is useful only for floating-point numbers of type BINARY_FLOAT or BINARY_DOUBLE. It instructs Oracle Database to return an alternative value n1 if the input value n2 is NaN (not a number). If n2 is not NaN, then Oracle returns n2. This function is useful for mapping NaN values to NULL.
     * <p>
     * This function takes as arguments any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. Oracle determines the argument with the highest numeric precedence, implicitly converts the remaining arguments to that datatype, and returns that datatype.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> nanvl(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.NANVL, BigDecimal.class);
    }

    /**
     * POWER returns n2 raised to the n1 power. The base n2 and the exponent n1 can be any numbers, but if n2 is negative, then n1 must be an integer.
     * <p>
     * This function takes as arguments any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. If any argument is BINARY_FLOAT or BINARY_DOUBLE, then the function returns BINARY_DOUBLE. Otherwise the function returns NUMBER.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> power(ColumnOrQuery<E, F, R> column, ColumnOrQuery<E, F, R> column2) {
        ColumnFunction<E, F, R, BigDecimal> rv = new ColumnFunction<>(column, DictFunctionOracle.POWER, BigDecimal.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, column2);
        return rv;
    }

    /**
     * REMAINDER returns the remainder of n2 divided by n1.
     * <p>
     * This function takes as arguments any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. Oracle determines the argument with the highest numeric precedence, implicitly converts the remaining arguments to that datatype, and returns that datatype.
     * <p>
     * The MOD function is similar to REMAINDER except that it uses FLOOR in its formula, whereas REMAINDER uses ROUND. Please refer to MOD.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> remainder(ColumnOrQuery<E, F, R> column, ColumnOrQuery<E, F, R> column2) {
        ColumnFunction<E, F, R, BigDecimal> rv = new ColumnFunction<>(column, DictFunctionOracle.REMAINDER, BigDecimal.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, column2);
        return rv;
    }

    /**
     * ROUND returns n rounded to integer places to the right of the decimal point. If you omit integer, then n is rounded to 0 places. The argument integer can be negative to round off digits left of the decimal point.
     * <p>
     * n can be any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. The argument integer must be an integer. If you omit integer, then the function returns the same datatype as the numeric datatype of the argument. If you include integer, then the function returns NUMBER.
     * <p>
     * For NUMBER values, the value n is rounded away from 0 (for example, to x+1 when x.5 is positive and to x-1 when x.5 is negative). For BINARY_FLOAT and BINARY_DOUBLE values, the function rounds to the nearest even value. Please refer to the examples that follow.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> round(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.ROUND);
    }

    /**
     * ROUND returns n rounded to integer places to the right of the decimal point. If you omit integer, then n is rounded to 0 places. The argument integer can be negative to round off digits left of the decimal point.
     * <p>
     * n can be any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. The argument integer must be an integer. If you omit integer, then the function returns the same datatype as the numeric datatype of the argument. If you include integer, then the function returns NUMBER.
     * <p>
     * For NUMBER values, the value n is rounded away from 0 (for example, to x+1 when x.5 is positive and to x-1 when x.5 is negative). For BINARY_FLOAT and BINARY_DOUBLE values, the function rounds to the nearest even value. Please refer to the examples that follow.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> round(ColumnOrQuery<E, F, R> column, Integer decimals) {
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(column, DictFunctionOracle.ROUND);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, decimals);
        return rv;
    }

    /**
     * SIGN returns the sign of n. This function takes as an argument any numeric datatype, or any nonnumeric datatype that can be implicitly converted to NUMBER, and returns NUMBER.
     * <p>
     * For value of NUMBER type, the sign is:
     * <p>
     * -1 if n<0
     * <p>
     * 0 if n=0
     * <p>
     * 1 if n>0
     * <p>
     * For binary floating-point numbers (BINARY_FLOAT and BINARY_DOUBLE), this function returns the sign bit of the number. The sign bit is:
     * <p>
     * -1 if n<0
     * <p>
     * +1 if n>=0 or n=NaN
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> sign(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.SIGN, BigDecimal.class);
    }

    /**
     * SIN returns the sine of n (an angle expressed in radians).
     * <p>
     * This function takes as an argument any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. If the argument is BINARY_FLOAT, then the function returns BINARY_DOUBLE. Otherwise the function returns the same numeric datatype as the argument.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> sin(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.SIN, BigDecimal.class);
    }

    /**
     * SINH returns the hyperbolic sine of n.
     * <p>
     * This function takes as an argument any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. If the argument is BINARY_FLOAT, then the function returns BINARY_DOUBLE. Otherwise the function returns the same numeric datatype as the argument.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> sinh(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.SINH, BigDecimal.class);
    }

    /**
     * SQRT returns the square root of n.
     * <p>
     * This function takes as an argument any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. The function returns the same datatype as the numeric datatype of the argument.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> sqrt(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.SQRT, BigDecimal.class);
    }

    /**
     *
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> sum(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.SUM);
    }

    /**
     * TAN returns the tangent of n (an angle expressed in radians).
     * This function takes as an argument any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. If the argument is BINARY_FLOAT, then the function returns BINARY_DOUBLE. Otherwise the function returns the same numeric datatype as the argument.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> tan(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.TAN, BigDecimal.class);
    }

    /**
     * TANH returns the hyperbolic tangent of n.
     * This function takes as an argument any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. If the argument is BINARY_FLOAT, then the function returns BINARY_DOUBLE. Otherwise the function returns the same numeric datatype as the argument.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> tanh(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.TANH, BigDecimal.class);
    }

    /**
     * The TRUNC (number) function returns n1 truncated to n2 decimal places. If n2 is omitted, then n1 is truncated to 0 places. n2 can be negative to truncate (make zero) n2 digits left of the decimal point.
     * This function takes as an argument any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. If you omit n2, then the function returns the same datatype as the numeric datatype of the argument. If you include n2, then the function returns NUMBER.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> trunc(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.TRUNC, BigDecimal.class);
    }

    /**
     * ADD_MONTHS returns the date date plus integer months. The date argument can be a datetime value or any value that can be implicitly converted to DATE. The integer argument can be an integer or any value that can be implicitly converted to an integer. The return type is always DATE, regardless of the datatype of date. If date is the last day of the month or if the resulting month has fewer days than the day component of date, then the result is the last day of the resulting month. Otherwise, the result has the same day component as date.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> addMonths(ColumnOrQuery<E, F, R> column, Integer number) {
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(column, DictFunctionOracle.ADD_MONTHS);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, number);
        return rv;
    }

    /**
     * CURRENT_DATE returns the current date in the session time zone, in a value in the Gregorian calendar of datatype DATE.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, java.sql.Date> currentDate() {
        return new NoParamFunction<>(DictFunctionOracle.CURRENT_DATE, java.sql.Date.class);
    }

    /**
     * CURRENT_TIMESTAMP returns the current date and time in the session time zone, in a value of datatype TIMESTAMP WITH TIME ZONE. The time zone offset reflects the current local time of the SQL session. If you omit precision, then the default is 6. The difference between this function and LOCALTIMESTAMP is that CURRENT_TIMESTAMP returns a TIMESTAMP WITH TIME ZONE value while LOCALTIMESTAMP returns a TIMESTAMP value.
     * In the optional argument, precision specifies the fractional second precision of the time value returned.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Timestamp> currentTimestamp() {
        return new NoParamFunction<>(DictFunctionOracle.CURRENT_TIMESTAMP, Timestamp.class);
    }

    /**
     * DBTIMEZONE returns the value of the database time zone. The return type is a time zone offset (a character type in the format '[+|-]TZH:TZM') or a time zone region name, depending on how the user specified the database time zone value in the most recent CREATE DATABASE or ALTER DATABASE statement.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> dbTimeZone() {
        return new NoParamFunction<>(DictFunctionOracle.DBTIMEZONE, String.class);
    }


    /**
     * EXTRACT extracts and returns the value of a specified datetime field from a datetime or interval value expression. When you extract a TIMEZONE_REGION or TIMEZONE_ABBR (abbreviation), the value returned is a string containing the appropriate time zone name or abbreviation. When you extract any of the other values, the value returned is in the Gregorian calendar. When extracting from a datetime with a time zone value, the value returned is in UTC. For a listing of time zone names and their corresponding abbreviations, query the V$TIMEZONE_NAMES dynamic performance view.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Long> extract(ColumnOrQuery<E, F, R> column, DatePeriod period) {
        ColumnFunction<E, F, R, Long> rv = new ColumnFunction<>(column, DictFunctionOracle.EXTRACT, Long.class);
        rv.setMandatoryKeyValue(ColumnFunction.BEFORE_COL, new FunctionKey(" from "));
        rv.setMandatoryKeyValue(ColumnFunction.BEFORE_COL2, period);
        return rv;
    }

    /**
     * FROM_TZ converts a timestamp value and a time zone to a TIMESTAMP WITH TIME ZONE value. time_zone_value is a character string in the format 'TZH:TZM' or a character expression that returns a string in TZR with optional TZD format.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Timestamp> fromTz(ColumnOrQuery<E, F, R> column, ColumnOrQuery<?, ?, ?> column2) {
        ColumnFunction<E, F, R, Timestamp> rv = new ColumnFunction<>(column, DictFunctionOracle.FROM_TZ, Timestamp.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, column2);
        return rv;
    }

    /**
     * FROM_TZ converts a timestamp value and a time zone to a TIMESTAMP WITH TIME ZONE value. time_zone_value is a character string in the format 'TZH:TZM' or a character expression that returns a string in TZR with optional TZD format.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Timestamp> fromTz(ColumnOrQuery<E, F, R> column, String timezone) {
        ColumnFunction<E, F, R, Timestamp> rv = new ColumnFunction<>(column, DictFunctionOracle.FROM_TZ, Timestamp.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, timezone);
        return rv;
    }

    /**
     * LAST_DAY returns the date of the last day of the month that contains date. The return type is always DATE, regardless of the datatype of date.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> lastDay(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.LAST_DAY, Integer.class);
    }

    /**
     * LOCALTIMESTAMP returns the current date and time in the session time zone in a value of datatype TIMESTAMP. The difference between this function and CURRENT_TIMESTAMP is that LOCALTIMESTAMP returns a TIMESTAMP value while CURRENT_TIMESTAMP returns a TIMESTAMP WITH TIME ZONE value.
     * <p>
     * The optional argument timestamp_precision specifies the fractional second precision of the time value returned.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Timestamp> localtimestamp() {
        return new NoParamFunction<>(DictFunctionOracle.LOCALTIMESTAMP, Timestamp.class);
    }

    /**
     * MONTHS_BETWEEN returns number of months between dates date1 and date2. If date1 is later than date2, then the result is positive. If date1 is earlier than date2, then the result is negative. If date1 and date2 are either the same days of the month or both last days of months, then the result is always an integer. Otherwise Oracle Database calculates the fractional portion of the result based on a 31-day month and considers the difference in time components date1 and date2.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> monthsBetween(ColumnOrQuery<E, F, R> column, ColumnOrQuery<E, F, R> column2) {
        ColumnFunction<E, F, R, Integer> rv = new ColumnFunction<>(column, DictFunctionOracle.MONTHS_BETWEEN, Integer.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, column2);
        return rv;
    }

    /**
     * NEW_TIME returns the date and time in time zone timezone2 when date and time in time zone timezone1 are date. Before using this function, you must set the NLS_DATE_FORMAT parameter to display 24-hour time. The return type is always DATE, regardless of the datatype of date.
     * <p>
     * The arguments timezone1 and timezone2 can be any of these text strings:
     * AST, ADT: Atlantic Standard or Daylight Time
     * BST, BDT: Bering Standard or Daylight Time
     * CST, CDT: Central Standard or Daylight Time
     * EST, EDT: Eastern Standard or Daylight Time
     * GMT: Greenwich Mean Time
     * HST, HDT: Alaska-Hawaii Standard Time or Daylight Time.
     * MST, MDT: Mountain Standard or Daylight Time
     * NST: Newfoundland Standard Time
     * PST, PDT: Pacific Standard or Daylight Time
     * YST, YDT: Yukon Standard or Daylight Time
     */
    default <E extends Entity, F extends Date, R extends EntityRelation> ColumnFunction<E, F, R, Timestamp> newTime(ColumnOrQuery<E, F, R> column, String timezone1, String timezone2) {
        ColumnFunction<E, F, R, Timestamp> rv = new ColumnFunction<>(column, DictFunctionOracle.MONTHS_BETWEEN, Timestamp.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, timezone1);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, timezone2);
        return rv;
    }


    /**
     * NEXT_DAY returns the date of the first weekday named by char that is later than the date date. The return type is always DATE, regardless of the datatype of date. The argument char must be a day of the week in the date language of your session, either the full name or the abbreviation. The minimum number of letters required is the number of letters in the abbreviated version. Any characters immediately following the valid abbreviation are ignored. The return value has the same hours, minutes, and seconds component as the argument date.
     */
    default <E extends Entity, F extends Date, R extends EntityRelation> ColumnFunction<E, F, R, F> nextDay(ColumnOrQuery<E, F, R> column, String weekDay) {
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(column, DictFunctionOracle.NEXT_DAY);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, weekDay);
        return rv;
    }

    /**
     * NUMTODSINTERVAL converts n to an INTERVAL DAY TO SECOND literal. The argument n can be any NUMBER value or an expression that can be implicitly converted to a NUMBER value. The argument interval_unit can be of CHAR, VARCHAR2, NCHAR, or NVARCHAR2 datatype. The value for interval_unit specifies the unit of n and must resolve to one of the following string values:
     * 'DAY'
     * 'HOUR'
     * 'MINUTE'
     * 'SECOND'
     * interval_unit is case insensitive. Leading and trailing values within the parentheses are ignored. By default, the precision of the return is 9.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> numtodsinterval(ColumnOrQuery<E, F, R> column, Integer n, String intervalunit) {
        ColumnFunction<E, F, R, Integer> rv = new ColumnFunction<>(column, DictFunctionOracle.NUMTODSINTERVAL, Integer.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, n);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, intervalunit);
        return rv;
    }

    /**
     * NUMTOYMINTERVAL converts number n to an INTERVAL YEAR TO MONTH literal. The argument n can be any NUMBER value or an expression that can be implicitly converted to a NUMBER value. The argument interval_unit can be of CHAR, VARCHAR2, NCHAR, or NVARCHAR2 datatype. The value for interval_unit specifies the unit of n and must resolve to one of the following string values:
     * <p>
     * 'YEAR'
     * 'MONTH'
     * <p>
     * interval_unit is case insensitive. Leading and trailing values within the parentheses are ignored. By default, the precision of the return is 9.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Integer> numtoyminterval(ColumnOrQuery<E, F, R> column, Integer n, String intervalunit) {
        ColumnFunction<E, F, R, Integer> rv = new ColumnFunction<>(column, DictFunctionOracle.NUMTOYMINTERVAL, Integer.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, n);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, intervalunit);
        return rv;
    }

    /**
     * ROUND returns date rounded to the unit specified by the format model fmt. The value returned is always of datatype DATE, even if you specify a different datetime datatype for date. If you omit fmt, then date is rounded to the nearest day. The date expression must resolve to a DATE value.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> round(ColumnOrQuery<E, F, R> column, DatePeriod period) {
        if (period == null) throw new MandatoryFunctionParameter(DictFunctionOracle.ROUND);
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(column, DictFunctionOracle.ROUND);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, new FunctionKey("'" + period + "'"));
        return rv;
    }

    /**
     * SESSIONTIMEZONE returns the time zone of the current session. The return type is a time zone offset (a character type in the format '[+|]TZH:TZM') or a time zone region name, depending on how the user specified the session time zone value in the most recent ALTER SESSION statement.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> sessionTimezone() {
        return new NoParamFunction<>(DictFunctionOracle.SESSIONTIMEZONE, String.class);
    }

    /**
     * TO_CHAR (datetime) converts a datetime or interval value of DATE, TIMESTAMP, TIMESTAMP WITH TIME ZONE, or TIMESTAMP WITH LOCAL TIME ZONE datatype to a value of VARCHAR2 datatype in the format specified by the date format fmt. If you omit fmt, then date is converted to a VARCHAR2 value as follows:
     * <p>
     * DATE values are converted to values in the default date format.
     * <p>
     * TIMESTAMP and TIMESTAMP WITH LOCAL TIME ZONE values are converted to values in the default timestamp format.
     * <p>
     * TIMESTAMP WITH TIME ZONE values are converted to values in the default timestamp with time zone format.
     * <p>
     * Please refer to "Format Models" for information on datetime formats.
     * <p>
     * The 'nlsparam' argument specifies the language in which month and day names and abbreviations are returned. This argument can have this form:
     * <p>
     * 'NLS_DATE_LANGUAGE = language'
     * <p>
     * If you omit 'nlsparam', then this function uses the default date language for your session.
     */
    default <E extends Entity, F extends Date, R extends EntityRelation> ColumnFunction<E, F, R, String> toChar(ColumnOrQuery<E, F, R> column, String format) {
        ColumnFunction<E, F, R, String> rv = new ColumnFunction<>(column, DictFunctionOracle.TO_CHAR, String.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, format);
        return rv;
    }

    /**
     * TO_TIMESTAMP converts char of CHAR, VARCHAR2, NCHAR, or NVARCHAR2 datatype to a value of TIMESTAMP datatype.
     * <p>
     * The optional fmt specifies the format of char. If you omit fmt, then char must be in the default format of the TIMESTAMP datatype, which is determined by the NLS_TIMESTAMP_FORMAT initialization parameter. The optional 'nlsparam' argument has the same purpose in this function as in the TO_CHAR function for date conversion.
     * <p>
     * This function does not support CLOB data directly. However, CLOBs can be passed in as arguments through implicit data conversion.
     */
    default <E extends Entity, R extends EntityRelation> ColumnFunction<E, String, R, Timestamp> toTimestamp(ColumnOrQuery<E, String, R> column, String format) {
        ColumnFunction<E, String, R, Timestamp> rv = new ColumnFunction<>(column, DictFunctionOracle.TO_TIMESTAMP, Timestamp.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, format);
        return rv;
    }

    /**
     * TO_TIMESTAMP_TZ converts char of CHAR, VARCHAR2, NCHAR, or NVARCHAR2 datatype to a value of TIMESTAMP WITH TIME ZONE datatype.
     * <p>
     * Note:
     * <p>
     * This function does not convert character strings to TIMESTAMP WITH LOCAL TIME ZONE. To do this, use a CAST function, as shown in CAST.
     * The optional fmt specifies the format of char. If you omit fmt, then char must be in the default format of the TIMESTAMP WITH TIME ZONE datatype. The optional 'nlsparam' has the same purpose in this function as in the TO_CHAR function for date conversion.
     */
    default <E extends Entity, R extends EntityRelation> ColumnFunction<E, String, R, Timestamp> toTimestampTz(ColumnOrQuery<E, String, R> column, String format) {
        ColumnFunction<E, String, R, Timestamp> rv = new ColumnFunction<>(column, DictFunctionOracle.TO_TIMESTAMP_TZ, Timestamp.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, format);
        return rv;
    }


    /**
     * TO_DSINTERVAL converts a character string of CHAR, VARCHAR2, NCHAR, or NVARCHAR2 datatype to an INTERVAL DAY TO SECOND value.
     * <p>
     * char is the character string to be converted.
     * <p>
     * The only valid nlsparam you can specify in this function is NLS_NUMERIC_CHARACTERS. This argument can have the form:
     * <p>
     * NLS_NUMERIC_CHARACTERS = "dg"
     * <p>
     * where d and g represent the decimal character and group separator respectively. Neither character can be a space.
     */
    default <E extends Entity, R extends EntityRelation> ColumnFunction<E, String, R, Timestamp> toDsinterval(String value, String nlsparam) {
        ColumnFunction<E, String, R, Timestamp> rv = new ColumnFunction<>(value, DictFunctionOracle.TO_DSINTERVAL, Timestamp.class);
        rv.setKeyValue(ColumnFunction.AFTER_COL, nlsparam);
        return rv;
    }

    /**
     * TO_DSINTERVAL converts a character string of CHAR, VARCHAR2, NCHAR, or NVARCHAR2 datatype to an INTERVAL DAY TO SECOND value.
     * <p>
     * char is the character string to be converted.
     * <p>
     * The only valid nlsparam you can specify in this function is NLS_NUMERIC_CHARACTERS. This argument can have the form:
     * <p>
     * NLS_NUMERIC_CHARACTERS = "dg"
     * <p>
     * where d and g represent the decimal character and group separator respectively. Neither character can be a space.
     */
    default <E extends Entity, R extends EntityRelation> ColumnFunction<E, String, R, Timestamp> toDsinterval(ColumnOrQuery<E, String, R> column) {
        ColumnFunction<E, String, R, Timestamp> rv = new ColumnFunction<>(column, DictFunctionOracle.TO_DSINTERVAL, Timestamp.class);
        return rv;
    }

    /**
     * TO_YMINTERVAL converts a character string of CHAR, VARCHAR2, NCHAR, or NVARCHAR2 datatype to an INTERVAL YEAR TO MONTH type, where char is the character string to be converted.
     */
    default <E extends Entity, R extends EntityRelation> ColumnFunction<E, String, R, Date> toYminterval(ColumnOrQuery<E, String, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.TO_YMINTERVAL, Date.class);
    }


    /**
     * The TRUNC (date) function returns date with the time portion of the day truncated to the unit specified by the format model fmt. The value returned is always of datatype DATE, even if you specify a different datetime datatype for date. If you omit fmt, then date is truncated to the nearest day. Please refer to "ROUND and TRUNC Date Functions" for the permitted format models to use in fmt.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> trunc(ColumnOrQuery<E, F, R> column, DatePeriod period) {
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(column, DictFunctionOracle.TRUNC);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, new FunctionKey("'" + period + "'"));
        return rv;
    }

    /**
     *
     */
    default <C extends Number, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> sysdate(ColumnOrQuery<E, F, R> column, Class<C> clazz) {
        return new ColumnFunction<>(column, DictFunctionOracle.SYSDATE, clazz);
    }

    /**
     *
     */
    default <C extends Number, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> sum(ColumnOrQuery<E, F, R> column, Class<C> clazz) {
        return new ColumnFunction<>(column, DictFunctionOracle.SUM, clazz);
    }

    /**
     *
     */
    default <C extends Number, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> sum(Class<C> clazz, Column<?, ?, ?>... columns) {
        return new ColumnFunction<>(DictFunctionOracle.SUM, clazz, "+", columns);
    }

    /**
     *
     */
    default <C extends Number, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, BigDecimal> sum(Column... columns) {
        return new ColumnFunction<>(DictFunctionOracle.SUM, BigDecimal.class, "+", columns);
    }


    /**
     *
     */
    default <C, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> count(ColumnOrQuery<E, F, R> column, Class<C> clazz) {
        return new ColumnFunction<>(column, DictFunctionOracle.COUNT, clazz);
    }

    /**
     *
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Long> count(E entity) {
        return new ColumnFunction<>(DictFunctionOracle.COUNT, Long.class, entity);
    }

    /**
     *
     */
    default <C, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> replace(ColumnOrQuery<E, F, R> column, Class<C> clazz) {
        return new ColumnFunction<>(column, DictFunctionOracle.REPLACE, clazz);
    }

    /**
     *
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> distinct(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.DISTINCT);
    }

    /**
     *
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Timestamp> interval(Number value, DatePeriod addUnit) {
        NoParamFunction<E, F, R, Timestamp> rv = new NoParamFunction<>(DictFunctionOracle.INTERVAL, Timestamp.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, value);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, addUnit);
        return rv;
    }


    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Timestamp> interval(ColumnOrQuery<E, F, R> columnOrQuery, Number value, DatePeriod addUnit) {
        ColumnFunction<E, F, R, Timestamp> rv = new ColumnFunction<>(columnOrQuery, DictFunctionOracle.INTERVAL, Timestamp.class);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, value);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL2, addUnit);
        return rv;
    }


    //TODO: other scenarios

    /**
     * AVG returns average value of expr.
     * This function takes as an argument any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. The function returns the same datatype as the numeric datatype of the argument.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> avg(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.AVG);
    }


    /**
     * COLLECT takes as its argument a column of any type and creates a nested table of the input type out of the rows selected. To get the results of this function you must use it within a CAST function.
     * If column is itself a collection, then the output of COLLECT is a nested table of collections.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> collect(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.COLLECT);
    }

    //TODO: other scenarios

    /**
     * CORR returns the coefficient of correlation of a set of number pairs. You can use it as an aggregate or analytic function.
     * <p>
     * This function takes as arguments any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. Oracle determines the argument with the highest numeric precedence, implicitly converts the remaining arguments to that datatype, and returns that datatype.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> corr(ColumnOrQuery<E, F, R> column, ColumnOrQuery<E, F, R> column2) {
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(column, DictFunctionOracle.CORR);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, column2);
        return rv;
    }

    /**
     * COVAR_POP returns the population covariance of a set of number pairs. You can use it as an aggregate or analytic function.
     * This function takes as arguments any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. Oracle determines the argument with the highest numeric precedence, implicitly converts the remaining arguments to that datatype, and returns that datatype.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> covarPop(ColumnOrQuery<E, F, R> column, ColumnOrQuery<E, F, R> column2) {
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(column, DictFunctionOracle.COVAR_POP);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, column2);
        return rv;
    }

    /**
     * COVAR_SAMP returns the sample covariance of a set of number pairs. You can use it as an aggregate or analytic function.
     * This function takes as arguments any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. Oracle determines the argument with the highest numeric precedence, implicitly converts the remaining arguments to that datatype, and returns that datatype.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> covarSamp(ColumnOrQuery<E, F, R> column, ColumnOrQuery<E, F, R> column2) {
        ColumnFunction<E, F, R, F> rv = new ColumnFunction<>(column, DictFunctionOracle.COVAR_SAMP);
        rv.setMandatoryKeyValue(ColumnFunction.AFTER_COL, column2);
        return rv;
    }

    //TODO: expand it

    /**
     * CUME_DIST calculates the cumulative distribution of a value in a group of values. The range of values returned by CUME_DIST is >0 to <=1. Tie values always evaluate to the same cumulative distribution value.
     * This function takes as arguments any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. Oracle Database determines the argument with the highest numeric precedence, implicitly converts the remaining arguments to that datatype, makes the calculation, and returns NUMBER.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> cumeDist(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.CUME_DIST);
    }

    /**
     * DENSE_RANK computes the rank of a row in an ordered group of rows and returns the rank as a NUMBER. The ranks are consecutive integers beginning with 1. The largest rank value is the number of unique values returned by the query. Rank values are not skipped in the event of ties. Rows with equal values for the ranking criteria receive the same rank. This function is useful for top-N and bottom-N reporting.
     * <p>
     * This function accepts as arguments any numeric datatype and returns NUMBER.
     * As an aggregate function, DENSE_RANK calculates the dense rank of a hypothetical row identified by the arguments of the function with respect to a given sort specification. The arguments of the function must all evaluate to constant expressions within each aggregate group, because they identify a single row within each group. The constant argument expressions and the expressions in the order_by_clause of the aggregate match by position. Therefore, the number of arguments must be the same and types must be compatible.
     * As an analytic function, DENSE_RANK computes the rank of each row returned from a query with respect to the other rows, based on the values of the value_exprs in the order_by_clause.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> denseRank(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.DENSE_RANK);
    }

    //TODO: expand it

    /**
     * FIRST and LAST are very similar functions. Both are aggregate and analytic functions that operate on a set of values from a set of rows that rank as the FIRST or LAST with respect to a given sorting specification. If only one row ranks as FIRST or LAST, the aggregate operates on the set with only one element.
     * <p>
     * This function takes as an argument any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. The function returns the same datatype as the numeric datatype of the argument.
     * When you need a value from the first or last row of a sorted group, but the needed value is not the sort key, the FIRST and LAST functions eliminate the need for self-joins or views and enable better performance.
     * The aggregate_function is any one of the MIN, MAX, SUM, AVG, COUNT, VARIANCE, or STDDEV functions. It operates on values from the rows that rank either FIRST or LAST. If only one row ranks as FIRST or LAST, the aggregate operates on a singleton (nonaggregate) set.
     * The KEEP keyword is for semantic clarity. It qualifies aggregate_function, indicating that only the FIRST or LAST values of aggregate_function will be returned.
     * DENSE_RANK FIRST or DENSE_RANK LAST indicates that Oracle Database will aggregate over only those rows with the minimum (FIRST) or the maximum (LAST) dense rank (also called olympic rank).
     * You can use the FIRST and LAST functions as analytic functions by specifying the OVER clause. The query_partitioning_clause is the only part of the OVER clause valid with these functions.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> first(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.FIRST);
    }

    /**
     * GROUP_ID distinguishes duplicate groups resulting from a GROUP BY specification. It is useful in filtering out duplicate groupings from the query result. It returns an Oracle NUMBER to uniquely identify duplicate groups. This function is applicable only in a SELECT statement that contains a GROUP BY clause.
     * <p>
     * If n duplicates exist for a particular grouping, then GROUP_ID returns numbers in the range 0 to n-1.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> groupId() {
        return new NoParamFunction<>(DictFunctionOracle.GROUP_ID);
    }

    /**
     * GROUPING distinguishes superaggregate rows from regular grouped rows. GROUP BY extensions such as ROLLUP and CUBE produce superaggregate rows where the set of all values is represented by null. Using the GROUPING function, you can distinguish a null representing the set of all values in a superaggregate row from a null in a regular row.
     * The expr in the GROUPING function must match one of the expressions in the GROUP BY clause. The function returns a value of 1 if the value of expr in the row is a null representing the set of all values. Otherwise, it returns zero. The datatype of the value returned by the GROUPING function is Oracle NUMBER. Please refer to the SELECT group_by_clause for a discussion of these terms.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> grouping(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.GROUPING);
    }

    /**
     * GROUPING_ID returns a number corresponding to the GROUPING bit vector associated with a row. GROUPING_ID is applicable only in a SELECT statement that contains a GROUP BY extension, such as ROLLUP or CUBE, and a GROUPING function. In queries with many GROUP BY expressions, determining the GROUP BY level of a particular row requires many GROUPING functions, which leads to cumbersome SQL. GROUPING_ID is useful in these cases.
     * GROUPING_ID is functionally equivalent to taking the results of multiple GROUPING functions and concatenating them into a bit vector (a string of ones and zeros). By using GROUPING_ID you can avoid the need for multiple GROUPING functions and make row filtering conditions easier to express. Row filtering is easier with GROUPING_ID because the desired rows can be identified with a single condition of GROUPING_ID = n. The function is especially useful when storing multiple levels of aggregation in a single table.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> groupingId(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.GROUPING_ID);
    }

    /**
     * FIRST and LAST are very similar functions. Both are aggregate and analytic functions that operate on a set of values from a set of rows that rank as the FIRST or LAST with respect to a given sorting specification. If only one row ranks as FIRST or LAST, the aggregate operates on the set with only one element.
     * This function takes as an argument any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. The function returns the same datatype as the numeric datatype of the argument.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> last(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.LAST);
    }

    /**
     * MAX returns maximum value of expr. You can use it as an aggregate or analytic function.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> max(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.MAX);
    }

    /**
     * MEDIAN is an inverse distribution function that assumes a continuous distribution model. It takes a numeric or datetime value and returns the middle value or an interpolated value that would be the middle value once the values are sorted. Nulls are ignored in the calculation.
     * This function takes as arguments any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. If you specify only expr, then the function returns the same datatype as the numeric datatype of the argument. if you specify the OVER clause, then Oracle Database determines the argument with the highest numeric precedence, implicitly converts the remaining arguments to that datatype, and returns that datatype.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> median(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.MEDIAN);
    }

    /**
     * MIN returns minimum value of expr. You can use it as an aggregate or analytic function.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> min(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.MIN);
    }

    //TODO: exp

    /**
     * PERCENTILE_CONT is an inverse distribution function that assumes a continuous distribution model. It takes a percentile value and a sort specification, and returns an interpolated value that would fall into that percentile value with respect to the sort specification. Nulls are ignored in the calculation.
     * This function takes as an argument any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. The function returns the same datatype as the numeric datatype of the argument.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> percentileCont(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.PERCENTILE_CONT);
    }

    /**
     * PERCENTILE_DISC is an inverse distribution function that assumes a discrete distribution model. It takes a percentile value and a sort specification and returns an element from the set. Nulls are ignored in the calculation.
     * This function takes as an argument any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. The function returns the same datatype as the numeric datatype of the argument.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> percentileDisc(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.PERCENTILE_DISC);
    }

    /**
     * PERCENT_RANK is similar to the CUME_DIST (cumulative distribution) function. The range of values returned by PERCENT_RANK is 0 to 1, inclusive. The first row in any set has a PERCENT_RANK of 0. The return value is NUMBER.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> percentRank(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.PERCENT_RANK);
    }

    /**
     * RANK calculates the rank of a value in a group of values. The return type is NUMBER.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> rank(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.RANK);
    }

    /**
     * VAR_POP returns the population variance of a set of numbers after discarding the nulls in this set. You can use it as both an aggregate and analytic function.
     * This function takes as an argument any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. The function returns the same datatype as the numeric datatype of the argument.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> varPop(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.VAR_POP);
    }

    /**
     * VAR_SAMP returns the sample variance of a set of numbers after discarding the nulls in this set. You can use it as both an aggregate and analytic function.
     * This function takes as an argument any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. The function returns the same datatype as the numeric datatype of the argument.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> varSamp(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.VAR_SAMP);
    }

    /**
     * VARIANCE returns the variance of expr. You can use it as an aggregate or analytic function.
     * <p>
     * Oracle Database calculates the variance of expr as follows:
     * 0 if the number of rows in expr = 1
     * VAR_SAMP if the number of rows in expr > 1
     * If you specify DISTINCT, then you can specify only the query_partition_clause of the analytic_clause. The order_by_clause and windowing_clause are not allowed.
     * This function takes as an argument any numeric datatype or any nonnumeric datatype that can be implicitly converted to a numeric datatype. The function returns the same datatype as the numeric datatype of the argument.
     */
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> variance(ColumnOrQuery<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunctionOracle.VARIANCE);
    }


}
