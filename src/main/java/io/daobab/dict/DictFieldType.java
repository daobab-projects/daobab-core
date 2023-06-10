package io.daobab.dict;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface DictFieldType {

    Class<BigDecimal> CLASS_BIG_DECIMAL = BigDecimal.class;
    Class<String> CLASS_STRING = String.class;
    Class<java.util.Date> CLASS_DATE_UTIL = java.util.Date.class;
    Class<Date> CLASS_DATE_SQL = Date.class;
    Class<Timestamp> CLASS_TIMESTAMP_SQL = Timestamp.class;
    Class<Time> CLASS_TIME_SQL = Time.class;
    Class<Double> CLASS_DOUBLE = Double.class;
    Class<Float> CLASS_FLOAT = Float.class;
    Class<BigInteger> CLASS_BIG_INTEGER = BigInteger.class;
    Class<Boolean> CLASS_BOOLEAN = Boolean.class;
    Class<Long> CLASS_LONG = Long.class;
    Class<Integer> CLASS_INTEGER_SMALL = int.class;
    Class<Integer> CLASS_INTEGER = Integer.class;
    Class<byte[]> CLASS_BYTE_ARRAY = byte[].class;
    Class<LocalDate> CLASS_LOCAL_DATE = LocalDate.class;
    Class<LocalDateTime> CLASS_LOCAL_DATE_TIME = LocalDateTime.class;
    Class<ZonedDateTime> CLASS_ZONED_DATE_TIME = ZonedDateTime.class;
}
