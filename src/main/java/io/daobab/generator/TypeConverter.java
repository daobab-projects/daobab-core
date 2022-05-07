package io.daobab.generator;

import io.daobab.dict.DictFieldType;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public interface TypeConverter {

    static Class<?> convert(int type, Integer size, Integer precision) {
        if (!isNumeric(type)) {
            return convert(type);
        }

//        if (precision==null||precision==0){
//            switch (type) {
//                case (Types.BIGINT):
//                case (Types.DECIMAL):
//                case (Types.DOUBLE):
//                case (Types.FLOAT):
//                case (Types.INTEGER):
//                case (Types.NUMERIC):
//                case (Types.SMALLINT):
//                case (Types.REAL):
//                case (Types.TINYINT):
//                    return true;
//                default:
//                    return false;
//            }
//        }

        return convert(type);
    }

    static boolean isNumeric(int type) {
        switch (type) {
            case (Types.BIGINT):
            case (Types.DECIMAL):
            case (Types.DOUBLE):
            case (Types.FLOAT):
            case (Types.INTEGER):
            case (Types.NUMERIC):
            case (Types.SMALLINT):
            case (Types.REAL):
            case (Types.TINYINT):
                return true;
            default:
                return false;
        }
    }

    static Class<?> convert(int type) {
        switch (type) {
            case (Types.ARRAY):
                return Object[].class;
            case (Types.BIGINT):
                return DictFieldType.CLASS_BIG_INTEGER;
            case (Types.BINARY):
            case (Types.BLOB):
                return DictFieldType.CLASS_BYTE_ARRAY;
            case (Types.BIT):
            case (Types.BOOLEAN):
                return DictFieldType.CLASS_BOOLEAN;
            case (Types.CHAR):
            case (Types.CLOB):
                return DictFieldType.CLASS_STRING;
            case (Types.DATE):
            case (Types.TIME_WITH_TIMEZONE):
                return Date.class;
            case (Types.DECIMAL):
            case (Types.FLOAT):
            case (Types.DOUBLE):
            case (Types.INTEGER):
            case (Types.NUMERIC):
                return DictFieldType.CLASS_BIG_DECIMAL;
            case (Types.LONGNVARCHAR):
            case (Types.LONGVARCHAR):
            case (Types.NCHAR):
            case (Types.NVARCHAR):
            case (Types.VARCHAR):
            case (Types.SQLXML):
            case (Types.NCLOB):
                return String.class;
            case (Types.LONGVARBINARY):
            case (Types.VARBINARY):
                return byte[].class;
            case (Types.REAL):
                return Float.class;
            case (Types.SMALLINT):
            case (Types.TINYINT):
                return Integer.class;
            case (Types.TIME):
                return Time.class;
            case (Types.TIMESTAMP):
            case (Types.TIMESTAMP_WITH_TIMEZONE):
                return Timestamp.class;
            case (Types.NULL):
            case (Types.OTHER):
            case (Types.REF):
            case (Types.REF_CURSOR):
            case (Types.ROWID):
            case (Types.STRUCT):
            case (Types.JAVA_OBJECT):
            case (Types.DISTINCT):
            case (Types.DATALINK):
            default:
                return Object.class;
        }

    }


    static String getDbTypeName(int type) {
        switch (type) {
            case (Types.ARRAY):
                return "ARRAY";
            case (Types.BIGINT):
                return "BIGINT";
            case (Types.BINARY):
                return "BINARY";
            case (Types.BIT):
                return "BIT";
            case (Types.BLOB):
                return "BLOB";
            case (Types.BOOLEAN):
                return "BOOLEAN";
            case (Types.CHAR):
                return "CHAR";
            case (Types.CLOB):
                return "CLOB";
            case (Types.DATALINK):
                return "DATALINK";
            case (Types.DATE):
                return "DATE";
            case (Types.DECIMAL):
                return "DECIMAL";
            case (Types.DISTINCT):
                return "DISTINCT";
            case (Types.DOUBLE):
                return "DOUBLE";
            case (Types.FLOAT):
                return "FLOAT";
            case (Types.INTEGER):
                return "INTEGER";
            case (Types.JAVA_OBJECT):
                return "JAVA_OBJECT";
            case (Types.LONGNVARCHAR):
                return "LONGNVARCHAR";
            case (Types.LONGVARBINARY):
                return "LONGVARBINARY";
            case (Types.LONGVARCHAR):
                return "LONGVARCHAR";
            case (Types.NCHAR):
                return "NCHAR";
            case (Types.NCLOB):
                return "NCLOB";
            case (Types.NULL):
                return "NULL";
            case (Types.NUMERIC):
                return "NUMERIC";
            case (Types.NVARCHAR):
                return "NVARCHAR";
            case (Types.OTHER):
                return "OTHER";
            case (Types.REAL):
                return "REAL";
            case (Types.REF):
                return "REF";
            case (Types.REF_CURSOR):
                return "REF_CURSOR";
            case (Types.ROWID):
                return "ROWID";
            case (Types.SMALLINT):
                return "SMALLINT";
            case (Types.SQLXML):
                return "SQLXML";
            case (Types.STRUCT):
                return "STRUCT";
            case (Types.TIME):
                return "TIME";
            case (Types.TIME_WITH_TIMEZONE):
                return "TIME_WITH_TIMEZONE";
            case (Types.TIMESTAMP):
                return "TIMESTAMP";
            case (Types.TIMESTAMP_WITH_TIMEZONE):
                return "TIMESTAMP_WITH_TIMEZONE";
            case (Types.TINYINT):
                return "TINYINT";
            case (Types.VARBINARY):
                return "VARBINARY";
            case (Types.VARCHAR):
                return "VARCHAR";
            default:
                return "";
        }

    }

    static String convertToTS(Class<?> clazz) {
        if (clazz.equals(String.class)) return "string";
        if (Number.class.isAssignableFrom(clazz)) return "number";
        if (isOneOf(clazz, Date.class, Timestamp.class, Date.class, Time.class)) return "Date";
        if (clazz.isInstance(Object[].class)) return "any[]";
        if (clazz.isInstance(byte[].class)) return "any";
        if (isOneOf(clazz, Boolean.class, boolean.class)) return "boolean";
        return "any";

    }

    static boolean isOneOf(Class<?> clazz, Class... classes) {
        if (clazz == null || classes == null) return false;
        for (Class<?> c : classes) {
            if (clazz.equals(c)) return true;
        }
        return false;
    }


}
