package io.daobab.generator;

import io.daobab.error.DaobabException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Covers the JDBC {@code Types} to Java class mapping used by the generator, plus the enforced/general
 * overrides, the numeric detection, the database type names and the TypeScript type mapping.
 */
class TestJDBCTypeConverter {

    @Test
    void mapsTheStandardJdbcTypes() {
        JDBCTypeConverter converter = new JDBCTypeConverter();

        assertEquals(String.class, converter.convert("T.C", Types.VARCHAR));
        assertEquals(String.class, converter.convert("T.C", Types.CHAR));
        assertEquals(String.class, converter.convert("T.C", Types.CLOB));
        assertEquals(BigInteger.class, converter.convert("T.C", Types.BIGINT));
        assertEquals(BigDecimal.class, converter.convert("T.C", Types.DECIMAL));
        //INTEGER maps to BigDecimal by design, SMALLINT/TINYINT to Integer, REAL to Float
        assertEquals(BigDecimal.class, converter.convert("T.C", Types.INTEGER));
        assertEquals(Integer.class, converter.convert("T.C", Types.SMALLINT));
        assertEquals(Integer.class, converter.convert("T.C", Types.TINYINT));
        assertEquals(Float.class, converter.convert("T.C", Types.REAL));
        assertEquals(Boolean.class, converter.convert("T.C", Types.BOOLEAN));
        assertEquals(Boolean.class, converter.convert("T.C", Types.BIT));
        assertEquals(byte[].class, converter.convert("T.C", Types.BLOB));
        assertEquals(byte[].class, converter.convert("T.C", Types.VARBINARY));
        assertEquals(LocalDate.class, converter.convert("T.C", Types.DATE));
        assertEquals(LocalTime.class, converter.convert("T.C", Types.TIME));
        assertEquals(LocalDateTime.class, converter.convert("T.C", Types.TIMESTAMP));
        assertEquals(ZonedDateTime.class, converter.convert("T.C", Types.TIMESTAMP_WITH_TIMEZONE));
        assertEquals(Object[].class, converter.convert("T.C", Types.ARRAY));
    }

    @Test
    void unknownTypeFallsBackToObject() {
        assertEquals(Object.class, new JDBCTypeConverter().convert("T.C", -424242));
    }

    @Test
    void enforcedTypeOverridesTheGeneralMapping() {
        JDBCTypeConverter converter = new JDBCTypeConverter()
                .setEnforcedTypeFor("USERS", "NAME", Integer.class);

        //the VARCHAR general mapping is overridden for USERS.NAME only
        assertEquals(Integer.class, converter.convert("USERS.NAME", Types.VARCHAR));
        assertEquals(String.class, converter.convert("USERS.OTHER", Types.VARCHAR));
    }

    @Test
    void generalConversionOverrideAppliesToEveryColumn() {
        JDBCTypeConverter converter = new JDBCTypeConverter()
                .setGeneralConversionFor(Types.INTEGER, Long.class);

        assertEquals(Long.class, converter.convert("ANY.COLUMN", Types.INTEGER));
    }

    @Test
    void convertWithSizeAndPrecisionDelegatesToTheTypeMapping() {
        JDBCTypeConverter converter = new JDBCTypeConverter();

        assertEquals(BigInteger.class, converter.convert("T", "C", Types.BIGINT, 10, 0));
        assertEquals(String.class, converter.convert("T", "C", Types.VARCHAR, 255, null));
    }

    @Test
    void nullTableOrColumnIsRejected() {
        JDBCTypeConverter converter = new JDBCTypeConverter();

        assertThrows(DaobabException.class, () -> converter.convert(null, "C", Types.VARCHAR, null, null));
        assertThrows(DaobabException.class, () -> converter.convert("T", null, Types.VARCHAR, null, null));
    }

    @Test
    void isNumericRecognisesTheNumericTypes() {
        JDBCTypeConverter converter = new JDBCTypeConverter();

        assertTrue(converter.isNumeric(Types.BIGINT));
        assertTrue(converter.isNumeric(Types.INTEGER));
        assertTrue(converter.isNumeric(Types.DECIMAL));
        assertTrue(converter.isNumeric(Types.REAL));
        assertFalse(converter.isNumeric(Types.VARCHAR));
        assertFalse(converter.isNumeric(Types.DATE));
    }

    @Test
    void databaseTypeNames() {
        assertEquals("VARCHAR", JDBCTypeConverter.getDataBaseTypeName(Types.VARCHAR));
        assertEquals("BIGINT", JDBCTypeConverter.getDataBaseTypeName(Types.BIGINT));
        assertEquals("TIMESTAMP_WITH_TIMEZONE", JDBCTypeConverter.getDataBaseTypeName(Types.TIMESTAMP_WITH_TIMEZONE));
        //an unmapped type yields an empty name
        assertEquals("", JDBCTypeConverter.getDataBaseTypeName(-424242));
    }

    @Test
    void typeScriptTypeMapping() {
        assertEquals("string", JDBCTypeConverter.convertToTS(String.class));
        assertEquals("number", JDBCTypeConverter.convertToTS(Integer.class));
        assertEquals("number", JDBCTypeConverter.convertToTS(BigDecimal.class));
        assertEquals("Date", JDBCTypeConverter.convertToTS(Timestamp.class));
        assertEquals("Date", JDBCTypeConverter.convertToTS(Time.class));
        assertEquals("boolean", JDBCTypeConverter.convertToTS(Boolean.class));
        assertEquals("boolean", JDBCTypeConverter.convertToTS(boolean.class));
        //anything unmapped falls back to 'any'
        assertEquals("any", JDBCTypeConverter.convertToTS(Character.class));
        assertEquals("any", JDBCTypeConverter.convertToTS(Object.class));
    }

    @Test
    void typeScriptArrayMapping() {
        //byte[] is a binary blob -> 'any', every other array -> 'any[]'
        assertEquals("any", JDBCTypeConverter.convertToTS(byte[].class));
        assertEquals("any[]", JDBCTypeConverter.convertToTS(Object[].class));
        assertEquals("any[]", JDBCTypeConverter.convertToTS(String[].class));
        assertEquals("any[]", JDBCTypeConverter.convertToTS(int[].class));
    }
}
