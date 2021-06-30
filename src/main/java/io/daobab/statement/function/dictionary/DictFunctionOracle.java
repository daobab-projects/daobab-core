package io.daobab.statement.function.dictionary;

@SuppressWarnings("unused")
public interface DictFunctionOracle {

    String INTERVAL = "INTERVAL";
    String DISTINCT = "DISTINCT";

    //Numeric Functions
    String ABS = "ABS";
    String ACOS = "ACOS";
    String ASIN = "ASIN";
    String ATAN = "ATAN";
    String ATAN2 = "ATAN2";
    String BITAND = "BITAND";
    String CEIL = "CEIL";
    String COS = "COS";
    String COSH = "COSH";
    String EXP = "EXP";
    String FLOOR = "FLOOR";
    String LN = "LN";
    String LOG = "LOG";
    String MOD = "MOD";
    String NANVL = "NANVL";
    String POWER = "POWER";
    String REMAINDER = "REMAINDER";
    String ROUND = "ROUND";
    String SIGN = "SIGN";
    String SIN = "SIN";
    String SINH = "SINH";
    String SQRT = "SQRT";
    String TAN = "TAN";
    String TANH = "TANH";
    String TRUNC = "TRUNC";
    String WIDTH_BUCKET = "WIDTH_BUCKET";

    //Character Functions Returning Character Values
    String CHR = "CHR";
    String CONCAT = "CONCAT";
    String INITCAP = "INITCAP";
    String LOWER = "LOWER";
    String LPAD = "LPAD";
    String LTRIM = "LTRIM";
    String NLS_INITCAP = "NLS_INITCAP";
    String NLS_LOWER = "NLS_LOWER";
    String NLSSORT = "NLSSORT";
    String NLS_UPPER = "NLS_UPPER";
    String REGEXP_REPLACE = "REGEXP_REPLACE";
    String REGEXP_SUBSTR = "REGEXP_SUBSTR";
    String REPLACE = "REPLACE";
    String RPAD = "RPAD";
    String RTRIM = "RTRIM";
    String SOUNDEX = "SOUNDEX";
    String SUBSTR = "SUBSTR";
    String TRANSLATE = "TRANSLATE";
    String TREAT = "TREAT";
    String TRIM = "TRIM";
    String UPPER = "UPPER";

    //    NLS Character Functions
    String NLS_CHARSET_DECL_LEN = "NLS_CHARSET_DECL_LEN";
    String NLS_CHARSET_ID = "NLS_CHARSET_ID";
    String NLS_CHARSET_NAME = "NLS_CHARSET_NAME";

    //    Character Functions Returning Number Values
    String ASCII = "ASCII";
    String INSTR = "INSTR";
    String LENGTH = "LENGTH";
    String REGEXP_INSTR = "REGEXP_INSTR";

    //    Datetime Functions
    String ADD_MONTHS = "ADD_MONTHS";
    String CURRENT_DATE = "CURRENT_DATE";
    String CURRENT_TIMESTAMP = "CURRENT_TIMESTAMP";
    String DBTIMEZONE = "DBTIMEZONE";
    String EXTRACT = "EXTRACT";
    String FROM_TZ = "FROM_TZ";
    String LAST_DAY = "LAST_DAY";
    String LOCALTIMESTAMP = "LOCALTIMESTAMP";
    String MONTHS_BETWEEN = "MONTHS_BETWEEN";
    String NEW_TIME = "NEW_TIME";
    String NEXT_DAY = "NEXT_DAY";
    String NUMTODSINTERVAL = "NUMTODSINTERVAL";
    String NUMTOYMINTERVAL = "NUMTOYMINTERVAL";
    String SESSIONTIMEZONE = "SESSIONTIMEZONE";
    String SYS_EXTRACT_UTC = "SYS_EXTRACT_UTC";
    String SYSDATE = "SYSDATE";
    String SYSTIMESTAMP = "SYSTIMESTAMP";
    String TO_CHAR = "TO_CHAR";
    String TO_TIMESTAMP = "TO_TIMESTAMP";
    String TO_TIMESTAMP_TZ = "TO_TIMESTAMP_TZ";
    String TO_DSINTERVAL = "TO_DSINTERVAL";
    String TO_YMINTERVAL = "TO_YMINTERVAL";
    String TZ_OFFSET = "TZ_OFFSET";

    //    General Comparison Functions
    String GREATEST = "GREATEST";
    String LEAST = "LEAST";

    //    Conversion Functions
    String ASCIISTR = "ASCIISTR";
    String BIN_TO_NUM = "BIN_TO_NUM";
    String CAST = "CAST";
    String CHARTOROWID = "CHARTOROWID";
    String COMPOSE = "COMPOSE";
    String CONVERT = "CONVERT";
    String DECOMPOSE = "DECOMPOSE";
    String HEXTORAW = "HEXTORAW";
    String RAWTOHEX = "RAWTOHEX";
    String RAWTONHEX = "RAWTONHEX";
    String ROWIDTOCHAR = "ROWIDTOCHAR";
    String ROWIDTONCHAR = "ROWIDTONCHAR";
    String SCN_TO_TIMESTAMP = "SCN_TO_TIMESTAMP";
    String TIMESTAMP_TO_SCN = "TIMESTAMP_TO_SCN";
    String TO_BINARY_DOUBLE = "TO_BINARY_DOUBLE";
    String TO_BINARY_FLOAT = "TO_BINARY_FLOAT";
    String TO_CLOB = "TO_CLOB";
    String TO_DATE = "TO_DATE";
    String TO_LOB = "TO_LOB";
    String TO_MULTI_BYTE = "TO_MULTI_BYTE";
    String TO_NCHAR = "TO_NCHAR";
    String TO_NCLOB = "TO_NCLOB";
    String TO_NUMBER = "TO_NUMBER";
    String TO_SINGLE_BYTE = "TO_SINGLE_BYTE";
    String UNISTR = "UNISTR";

    //    Large Object Functions
    String BFILENAME = "BFILENAME";
    String EMPTY_BLOB = "EMPTY_BLOB";
    String EMPTY_CLOB = "EMPTY_CLOB";

    //    Aggregate Functions
    String AVG = "AVG";
    String COLLECT = "COLLECT";
    String CORR = "CORR";
    String COUNT = "COUNT";
    String COVAR_POP = "COVAR_POP";
    String COVAR_SAMP = "COVAR_SAMP";
    String CUME_DIST = "CUME_DIST";
    String DENSE_RANK = "DENSE_RANK";
    String FIRST = "FIRST";
    String GROUP_ID = "GROUP_ID";
    String GROUPING = "GROUPING";
    String GROUPING_ID = "GROUPING_ID";
    String LAST = "LAST";
    String MAX = "MAX";
    String MEDIAN = "MEDIAN";
    String MIN = "MIN";
    String PERCENTILE_CONT = "PERCENTILE_CONT";
    String PERCENTILE_DISC = "PERCENTILE_DISC";
    String PERCENT_RANK = "PERCENT_RANK";
    String RANK = "RANK";
    String REGR = "REGR";
    String STATS_BINOMIAL_TEST = "STATS_BINOMIAL_TEST";
    String STATS_CROSSTAB = "STATS_CROSSTAB";
    String STATS_F_TEST = "STATS_F_TEST";
    String STATS_KS_TEST = "STATS_KS_TEST";
    String STATS_MODE = "STATS_MODE";
    String STATS_MW_TEST = "STATS_MW_TEST";
    String STATS_ONE_WAY_ANOVA = "STATS_ONE_WAY_ANOVA";
    String STATS_T_TEST = "STATS_T_TEST";
    String STATS_WSR_TEST = "STATS_WSR_TEST";
    String STDDEV = "STDDEV";
    String STDDEV_POP = "STDDEV_POP";
    String STDDEV_SAMP = "STDDEV_SAMP";
    String SUM = "SUM";
    String VAR_POP = "VAR_POP";
    String VAR_SAMP = "VAR_SAMP";
    String VARIANCE = "VARIANCE";


}
