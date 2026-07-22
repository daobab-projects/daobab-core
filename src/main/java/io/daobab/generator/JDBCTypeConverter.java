package io.daobab.generator;


import io.daobab.error.DaobabException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JDBCTypeConverter {

    public static final String UNKNOWN_TABLE = "%$@#$";

    private final Map<String, Class<?>> enforcedTypes = new HashMap<>();
    private final Map<Integer, Class<?>> generalTypes;

    public JDBCTypeConverter() {
        generalTypes = new HashMap<>();
        generalTypes.put(Types.ARRAY, Object[].class);
        generalTypes.put(Types.BIGINT, BigInteger.class);
        generalTypes.put(Types.BINARY, byte[].class);
        generalTypes.put(Types.BLOB, byte[].class);
        generalTypes.put(Types.BIT, Boolean.class);
        generalTypes.put(Types.BOOLEAN, Boolean.class);
        generalTypes.put(Types.CHAR, String.class);
        generalTypes.put(Types.CLOB, String.class);
        generalTypes.put(Types.DATE, LocalDate.class);
        generalTypes.put(Types.TIME_WITH_TIMEZONE, LocalTime.class);
        generalTypes.put(Types.DECIMAL, BigDecimal.class);
        generalTypes.put(Types.FLOAT, BigDecimal.class);
        generalTypes.put(Types.DOUBLE, BigDecimal.class);
        generalTypes.put(Types.INTEGER, BigDecimal.class);
        generalTypes.put(Types.NUMERIC, BigDecimal.class);
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
        generalTypes.put(Types.TIME, LocalTime.class);
        generalTypes.put(Types.TIMESTAMP, LocalDateTime.class);
        generalTypes.put(Types.TIMESTAMP_WITH_TIMEZONE, ZonedDateTime.class);
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
        return switch (type) {
            case (Types.ARRAY) -> "ARRAY";
            case (Types.BIGINT) -> "BIGINT";
            case (Types.BINARY) -> "BINARY";
            case (Types.BIT) -> "BIT";
            case (Types.BLOB) -> "BLOB";
            case (Types.BOOLEAN) -> "BOOLEAN";
            case (Types.CHAR) -> "CHAR";
            case (Types.CLOB) -> "CLOB";
            case (Types.DATALINK) -> "DATALINK";
            case (Types.DATE) -> "DATE";
            case (Types.DECIMAL) -> "DECIMAL";
            case (Types.DISTINCT) -> "DISTINCT";
            case (Types.DOUBLE) -> "DOUBLE";
            case (Types.FLOAT) -> "FLOAT";
            case (Types.INTEGER) -> "INTEGER";
            case (Types.JAVA_OBJECT) -> "JAVA_OBJECT";
            case (Types.LONGNVARCHAR) -> "LONGNVARCHAR";
            case (Types.LONGVARBINARY) -> "LONGVARBINARY";
            case (Types.LONGVARCHAR) -> "LONGVARCHAR";
            case (Types.NCHAR) -> "NCHAR";
            case (Types.NCLOB) -> "NCLOB";
            case (Types.NULL) -> "NULL";
            case (Types.NUMERIC) -> "NUMERIC";
            case (Types.NVARCHAR) -> "NVARCHAR";
            case (Types.OTHER) -> "OTHER";
            case (Types.REAL) -> "REAL";
            case (Types.REF) -> "REF";
            case (Types.REF_CURSOR) -> "REF_CURSOR";
            case (Types.ROWID) -> "ROWID";
            case (Types.SMALLINT) -> "SMALLINT";
            case (Types.SQLXML) -> "SQLXML";
            case (Types.STRUCT) -> "STRUCT";
            case (Types.TIME) -> "TIME";
            case (Types.TIME_WITH_TIMEZONE) -> "TIME_WITH_TIMEZONE";
            case (Types.TIMESTAMP) -> "TIMESTAMP";
            case (Types.TIMESTAMP_WITH_TIMEZONE) -> "TIMESTAMP_WITH_TIMEZONE";
            case (Types.TINYINT) -> "TINYINT";
            case (Types.VARBINARY) -> "VARBINARY";
            case (Types.VARCHAR) -> "VARCHAR";
            default -> "";
        };

    }

    public static String convertToTS(Class<?> clazz) {
        if (clazz.equals(String.class)) return "string";
        if (Number.class.isAssignableFrom(clazz)) return "number";
        if (isOneOf(clazz, Date.class, Timestamp.class, Date.class, Time.class)) return "Date";
        if (clazz.equals(byte[].class)) return "any";
        if (clazz.isArray()) return "any[]";
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

    public JDBCTypeConverter setEnforcedTypeFor(final String tableName, final String columnName, final Class<?> enforcedType) {
        enforcedTypes.put(getTableDotColumn(tableName, columnName), enforcedType);
        return this;
    }

    public JDBCTypeConverter setGeneralConversionFor(int jdbcType, final Class<?> enforcedType) {
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
