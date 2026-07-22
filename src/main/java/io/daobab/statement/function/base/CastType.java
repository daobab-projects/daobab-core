package io.daobab.statement.function.base;

/**
 * The target data type of a SQL {@code CAST(column AS type)} expression.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public enum CastType {
    /**
     * Binary.
     */
    BINARY,
    /** Character string. */
    CHAR,
    /** Date. */
    DATE,
    /** Date and time. */
    DATETIME,
    /** Time. */
    TIME,
    /** Decimal number. */
    DECIMAL,
    /** Signed integer. */
    SIGNED,
    /** Unsigned integer. */
    UNSIGNED

}
