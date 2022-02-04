package io.daobab.target.database.connection;

import java.sql.Types;

public enum JdbcType {

    BIT(Types.BIT),
    TINYINT(Types.TINYINT),
    SMALLINT(Types.SMALLINT),
    INTEGER(Types.INTEGER),
    BIGINT(Types.BIGINT),
    FLOAT(Types.FLOAT),
    REAL(Types.REAL),
    DOUBLE(Types.DOUBLE),
    NUMERIC(Types.NUMERIC),
    DECIMAL(Types.DECIMAL),
    CHAR(Types.CHAR),
    VARCHAR(Types.VARCHAR),
    LONGVARCHAR(Types.LONGVARCHAR),
    DATE(Types.DATE),
    TIME(Types.TIME),
    TIMESTAMP(Types.TIMESTAMP),
    BINARY(Types.BINARY),
    VARBINARY(Types.VARBINARY),
    LONGVARBINARY(Types.LONGVARBINARY),
    NULL(Types.NULL),
    OTHER(Types.OTHER),
    JAVA_OBJECT(Types.JAVA_OBJECT),
    DISTINCT(Types.DISTINCT),
    STRUCT(Types.STRUCT),
    ARRAY(Types.ARRAY),
    BLOB(Types.BLOB),
    CLOB(Types.CLOB),
    REF(Types.REF),
    DATALINK(Types.DATALINK),
    BOOLEAN(Types.BOOLEAN),
    ROWID(Types.ROWID),
    NCHAR(Types.NCHAR),
    NVARCHAR(Types.NVARCHAR),
    LONGNVARCHAR(Types.LONGNVARCHAR),
    NCLOB(Types.NCLOB),
    SQLXML(Types.SQLXML),
    REF_CURSOR(Types.REF_CURSOR),
    TIME_WITH_TIMEZONE(Types.TIME_WITH_TIMEZONE),
    TIMESTAMP_WITH_TIMEZONE(Types.TIMESTAMP_WITH_TIMEZONE),
    UNKNOWN(9999);


    public int getType() {
        return type;
    }

    int type;

    JdbcType(int type) {
        this.type = type;
    }

    JdbcType(String type) {
        this.type = Integer.parseInt(type);
    }

    public static JdbcType valueOf(int type) {
        switch (type) {
            case (Types.BIT):
                return BIT;
            case (Types.TINYINT):
                return TINYINT;
            case (Types.SMALLINT):
                return SMALLINT;
            case (Types.INTEGER):
                return INTEGER;
            case (Types.BIGINT):
                return BIGINT;
            case (Types.FLOAT):
                return FLOAT;
            case (Types.REAL):
                return REAL;
            case (Types.DOUBLE):
                return DOUBLE;
            case (Types.NUMERIC):
                return NUMERIC;
            case (Types.DECIMAL):
                return DECIMAL;
            case (Types.CHAR):
                return CHAR;
            case (Types.VARCHAR):
                return VARCHAR;
            case (Types.LONGVARCHAR):
                return LONGVARCHAR;
            case (Types.DATE):
                return DATE;
            case (Types.TIME):
                return TIME;
            case (Types.TIMESTAMP):
                return TIMESTAMP;
            case (Types.BINARY):
                return BINARY;
            case (Types.VARBINARY):
                return VARBINARY;
            case (Types.LONGVARBINARY):
                return LONGVARBINARY;
            case (Types.NULL):
                return NULL;
            case (Types.OTHER):
                return OTHER;
            case (Types.JAVA_OBJECT):
                return JAVA_OBJECT;
            case (Types.DISTINCT):
                return DISTINCT;
            case (Types.STRUCT):
                return STRUCT;
            case (Types.ARRAY):
                return ARRAY;
            case (Types.BLOB):
                return BLOB;
            case (Types.CLOB):
                return CLOB;
            case (Types.REF):
                return REF;
            case (Types.DATALINK):
                return DATALINK;
            case (Types.BOOLEAN):
                return BOOLEAN;
            case (Types.ROWID):
                return ROWID;
            case (Types.NCHAR):
                return NCHAR;
            case (Types.NVARCHAR):
                return NVARCHAR;
            case (Types.LONGNVARCHAR):
                return LONGNVARCHAR;
            case (Types.NCLOB):
                return NCLOB;
            case (Types.SQLXML):
                return SQLXML;
            case (Types.REF_CURSOR):
                return REF_CURSOR;
            case (Types.TIME_WITH_TIMEZONE):
                return TIME_WITH_TIMEZONE;
            case (Types.TIMESTAMP_WITH_TIMEZONE):
                return TIMESTAMP_WITH_TIMEZONE;
            default:
                return UNKNOWN;
        }
    }
}
