package io.daobab.generator;

import io.daobab.dict.DictFieldType;
import io.daobab.error.DaobabException;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public class TypeConverter {

    public static final String UNKNOWN_TABLE = "%$@#$";

    private final Map<String, Class<?>> enforcedTypes = new HashMap<>();
    private final Map<Integer, Class<?>> generalTypes;

    {
        generalTypes = new HashMap<>();
        generalTypes.put(Types.ARRAY, Object[].class);
        generalTypes.put(Types.BIGINT, DictFieldType.CLASS_BIG_INTEGER);
        generalTypes.put(Types.BINARY, DictFieldType.CLASS_BYTE_ARRAY);
        generalTypes.put(Types.BLOB, DictFieldType.CLASS_BYTE_ARRAY);
        generalTypes.put(Types.BIT, DictFieldType.CLASS_BOOLEAN);
        generalTypes.put(Types.BOOLEAN, DictFieldType.CLASS_BOOLEAN);
        generalTypes.put(Types.CHAR, DictFieldType.CLASS_STRING);
        generalTypes.put(Types.CLOB, DictFieldType.CLASS_STRING);
        generalTypes.put(Types.DATE, Date.class);
        generalTypes.put(Types.TIME_WITH_TIMEZONE, Date.class);
        generalTypes.put(Types.DECIMAL, DictFieldType.CLASS_BIG_DECIMAL);
        generalTypes.put(Types.FLOAT, DictFieldType.CLASS_BIG_DECIMAL);
        generalTypes.put(Types.DOUBLE, DictFieldType.CLASS_BIG_DECIMAL);
        generalTypes.put(Types.INTEGER, DictFieldType.CLASS_BIG_DECIMAL);
        generalTypes.put(Types.NUMERIC, DictFieldType.CLASS_BIG_DECIMAL);
        generalTypes.put(Types.LONGNVARCHAR, String.class);
        generalTypes.put(Types.LONGVARCHAR, String.class);
        generalTypes.put(Types.NCHAR, String.class);
        generalTypes.put(Types.NVARCHAR, String.class);
        generalTypes.put(Types.VARCHAR, String.class);
        generalTypes.put(Types.SQLXML, String.class);
        generalTypes.put(Types.NCLOB, String.class);
        generalTypes.put(Types.LONGVARBINARY, byte[].class);
        generalTypes.put(Types.VARBINARY, byte[].class);
        generalTypes.put(Types.REAL, Float.class);
        generalTypes.put(Types.SMALLINT, Integer.class);
        generalTypes.put(Types.TINYINT, Integer.class);
        generalTypes.put(Types.TIME, Time.class);
        generalTypes.put(Types.TIMESTAMP, Timestamp.class);
        generalTypes.put(Types.TIMESTAMP_WITH_TIMEZONE, Timestamp.class);
        generalTypes.put(Types.NULL, Object.class);
        generalTypes.put(Types.OTHER, Object.class);
        generalTypes.put(Types.REF, Object.class);
        generalTypes.put(Types.REF_CURSOR, Object.class);
        generalTypes.put(Types.ROWID, Object.class);
        generalTypes.put(Types.STRUCT, Object.class);
        generalTypes.put(Types.JAVA_OBJECT, Object.class);
        generalTypes.put(Types.DISTINCT, Object.class);
        generalTypes.put(Types.DATALINK, Object.class);

    }

    public static String getDataBaseTypeName(int type) {
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

    public static String convertToTS(Class<?> clazz) {
        if (clazz.equals(String.class)) return "string";
        if (Number.class.isAssignableFrom(clazz)) return "number";
        if (isOneOf(clazz, Date.class, Timestamp.class, Date.class, Time.class)) return "Date";
        if (clazz.isInstance(Object[].class)) return "any[]";
        if (clazz.isInstance(byte[].class)) return "any";
        if (isOneOf(clazz, Boolean.class, boolean.class)) return "boolean";
        return "any";

    }

    public static boolean isOneOf(Class<?> clazz, Class... classes) {
        if (clazz == null || classes == null) return false;
        for (Class<?> c : classes) {
            if (clazz.equals(c)) return true;
        }
        return false;
    }

    private String getTableDotColumn(String tableName, String columnName) {
        if (tableName == null) {
            throw new DaobabException("table name cannot be null");
        }
        if (columnName == null) {
            throw new DaobabException("column name cannot be null");
        }
        return tableName + "." + columnName;
    }

    public TypeConverter setEnforcedTypeFor(final String tableName, final String columnName, final Class<?> enforcedType) {
        enforcedTypes.put(getTableDotColumn(tableName, columnName), enforcedType);
        return this;
    }

    public TypeConverter setGeneralConversionFor(int jdbcType, final Class<?> enforcedType) {
        generalTypes.put(jdbcType, enforcedType);
        return this;
    }

    public Class<?> convert(String tableName, String columnName, int type, Integer size, Integer precision) {
        String tableDotColumn = getTableDotColumn(tableName, columnName);

        if (!isNumeric(type)) {
            return convert(tableDotColumn, type);
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

        return convert(tableDotColumn, type);
    }

    public boolean isNumeric(int type) {
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

    public Class<?> convert(String tableName, GenerateColumn generateColumn) {
        return convert(getTableDotColumn(tableName, generateColumn.getColumnName()), generateColumn.getDataType());
    }

    public Class<?> convert(String tableDotColumn, int type) {
        if (enforcedTypes.containsKey(tableDotColumn)) {
            return enforcedTypes.get(tableDotColumn);
        }

        return generalTypes.computeIfAbsent(type, k -> Object.class);
    }


}
