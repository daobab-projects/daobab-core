package io.daobab.statement.function.dictionary;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings("unused")
public interface DictFunctionH2 {

    String INTERVAL = "INTERVAL";
    String DISTINCT = "DISTINCT";
    String COUNT = "COUNT";

    String SUM = "SUM";
    String SUM2 = "";
    String SUB2 = "";
    String SUB = "SUB";


    //String Functions
    String ASCII = "ASCII";
    String BIT_LENGTH = "BIT_LENGTH";
    String LENGTH = "LENGTH";
    String OCTET_LENGTH = "OCTET_LENGTH";
    String CHAR = "CHAR";
    String CONCAT = "CONCAT";
    String CONCAT_WS = "CONCAT_WS";
    String DIFFERENCE = "DIFFERENCE";
    String HEXTORAW = "HEXTORAW";
    String RAWTOHEX = "RAWTOHEX";
    String INSTR = "INSTR";
    String INSERT_Function = "INSERT_Function";
    String LOWER = "LOWER";
    String UPPER = "UPPER";
    String LEFT = "LEFT";
    String RIGHT = "RIGHT";
    String LOCATE = "LOCATE";
    String POSITION = "POSITION";
    String LPAD = "LPAD";
    String RPAD = "RPAD";
    String LTRIM = "LTRIM";
    String RTRIM = "RTRIM";
    String TRIM = "TRIM";
    String REGEXP_REPLACE = "REGEXP_REPLACE";
    String REGEXP_LIKE = "REGEXP_LIKE";
    String REPEAT = "REPEAT";
    String REPLACE = "REPLACE";
    String SOUNDEX = "SOUNDEX";
    String SPACE = "SPACE";
    String STRINGDECODE = "STRINGDECODE";
    String STRINGENCODE = "STRINGENCODE";
    String STRINGTOUTF8 = "STRINGTOUTF8";
    String SUBSTRING = "SUBSTRING";
    String UTF8TOSTRING = "UTF8TOSTRING";
    String QUOTE_IDENT = "QUOTE_IDENT";
    String XMLATTR = "XMLATTR";
    String XMLNODE = "XMLNODE";
    String XMLCOMMENT = "XMLCOMMENT";
    String XMLCDATA = "XMLCDATA";
    String XMLSTARTDOC = "XMLSTARTDOC";
    String XMLTEXT = "XMLTEXT";
    String TO_CHAR = "TO_CHAR";
    String TRANSLATE = "TRANSLATE";

    //Numeric Functions
    String ABS = "ABS";
    String ACOS = "ACOS";
    String ASIN = "ASIN";
    String ATAN = "ATAN";
    String AVG = "AVG";//Returns the average value of an expression
    String ANY = "ANY";
    String COS = "COS";
    String COSH = "COSH";
    String COT = "COT";
    String SIN = "SIN";
    String SINH = "SINH";
    String TAN = "TAN";
    String TANH = "TANH";
    String ATAN2 = "ATAN2";
    String BITAND = "BITAND";
    String BITGET = "BITGET";
    String BITNOT = "BITNOT";
    String BITOR = "BITOR";
    String BITXOR = "BITXOR";
    String LSHIFT = "LSHIFT";
    String RSHIFT = "RSHIFT";
    String MOD = "MOD";
    String CEILING = "CEILING";
    String DEGREES = "DEGREES";
    String EXP = "EXP";
    String FLOOR = "FLOOR";
    String LN = "LN";
    String LOG = "LOG";
    String LOG10 = "LOG10";
    String MAX = "MAX";//Returns the maximum value in a set of values
    String MIN = "MIN";//Returns the minimum value in a set of values
    String ORA_HASH = "ORA_HASH";
    String RADIANS = "RADIANS";
    String SQRT = "SQRT";
    String PI = "PI";
    String POWER = "POWER";
    String RAND = "RAND";
    String RANDOM_UUID = "RANDOM_UUID";
    String ROUND = "ROUND";
    String ROUNDMAGIC = "ROUNDMAGIC";
    String SECURE_RAND = "SECURE_RAND";
    String SIGN = "SIGN";
    String ENCRYPT = "ENCRYPT";
    String DECRYPT = "DECRYPT";
    String HASH = "HASH";
    String TRUNCATE = "TRUNCATE";
    String COMPRESS = "COMPRESS";
    String EXPAND = "EXPAND";
    String ZERO = "ZERO";

    //Time and Date Functions
    String CURRENT_DATE = "CURRENT_DATE";
    String CURRENT_TIME = "CURRENT_TIME";
    String CURRENT_TIMESTAMP = "CURRENT_TIMESTAMP";
    String LOCALTIME = "LOCALTIME";
    String LOCALTIMESTAMP = "LOCALTIMESTAMP";
    String DATEADD = "DATEADD";
    String DATEDIFF = "DATEDIFF";
    String DAYNAME = "DAYNAME";
    String DAY_OF_MONTH = "DAY_OF_MONTH";
    String DAY_OF_WEEK = "DAY_OF_WEEK";
    String ISO_DAY_OF_WEEK = "ISO_DAY_OF_WEEK";
    String DAY_OF_YEAR = "DAY_OF_YEAR";
    String EXTRACT = "EXTRACT";
    String FORMATDATETIME = "FORMATDATETIME";
    String HOUR = "HOUR";
    String MINUTE = "MINUTE";
    String MONTH = "MONTH";
    String MONTHNAME = "MONTHNAME";
    String PARSEDATETIME = "PARSEDATETIME";
    String QUARTER = "QUARTER";
    String SECOND = "SECOND";
    String WEEK = "WEEK";
    String ISO_WEEK = "ISO_WEEK";
    String YEAR = "YEAR";
    String ISO_YEAR = "ISO_YEAR";


    //System Functions
    String ARRAY_GET = "ARRAY_GET";
    String ARRAY_LENGTH = "ARRAY_LENGTH";
    String ARRAY_CONTAINS = "ARRAY_CONTAINS";
    String ARRAY_CAT = "ARRAY_CAT";
    String ARRAY_APPEND = "ARRAY_APPEND";
    String ARRAY_SLICE = "ARRAY_SLICE";
    String AUTOCOMMIT = "AUTOCOMMIT";
    String CANCEL_SESSION = "CANCEL_SESSION";
    String CASEWHEN_Function = "CASEWHEN_Function";
    String CAST = "CAST";
    String COALESCE = "COALESCE";
    String CONVERT = "CONVERT";
    String CURRVAL = "CURRVAL";
    String CSVREAD = "CSVREAD";
    String CSVWRITE = "CSVWRITE";
    String CURRENT_SCHEMA = "CURRENT_SCHEMA";
    String CURRENT_CATALOG = "CURRENT_CATALOG";
    String DATABASE_PATH = "DATABASE_PATH";
    String DECODE = "DECODE";
    String DISK_SPACE_USED = "DISK_SPACE_USED";
    String SIGNAL = "SIGNAL";
    String ESTIMATED_ENVELOPE = "ESTIMATED_ENVELOPE";
    String FILE_READ = "FILE_READ";
    String FILE_WRITE = "FILE_WRITE";
    String GREATEST = "GREATEST";
    String IDENTITY = "IDENTITY";
    String IFNULL = "IFNULL";
    String LEAST = "LEAST";
    String LOCK_MODE = "LOCK_MODE";
    String LOCK_TIMEOUT = "LOCK_TIMEOUT";
    String LINK_SCHEMA = "LINK_SCHEMA";
    String MEMORY_FREE = "MEMORY_FREE";
    String MEMORY_USED = "MEMORY_USED";
    String NEXTVAL = "NEXTVAL";
    String NULLIF = "NULLIF";
    String NVL2 = "NVL2";
    String READONLY = "READONLY";
    String ROWNUM = "ROWNUM";
    String SCOPE_IDENTITY = "SCOPE_IDENTITY";
    String SESSION_ID = "SESSION_ID";
    String SET = "SET";
    String TABLE = "TABLE";
    String TRANSACTION_ID = "TRANSACTION_ID";
    String TRUNCATE_VALUE = "TRUNCATE_VALUE";
    String UNNEST = "UNNEST";
    String USER = "USER";
    String H2VERSION = "H2VERSION";


    //    String AVG="";
//    String MAX="";
//    String MIN="";
//    String SUM="";
    String EVERY = "EVERY";
    //    String ANY="";
//    String COUNT="";
    String STDDEV_POP = "STDDEV_POP";
    String STDDEV_SAMP = "STDDEV_SAMP";
    String VAR_POP = "VAR_POP";
    String VAR_SAMP = "VAR_SAMP";
    String BIT_AND = "BIT_AND";
    String BIT_OR = "BIT_OR";
    String SELECTIVITY = "SELECTIVITY";
    String ENVELOPE = "ENVELOPE";


    String ROW_NUMBER = "ROW_NUMBER";
}
