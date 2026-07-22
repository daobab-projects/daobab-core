package io.daobab.query.base;

/**
 * The kind of result a query produces.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public enum QueryType {

    /**
     * A query over whole entities (e.g. {@code select(tabCustomer)}).
     */
    ENTITY,
    /** A query over a single column (e.g. {@code select(tabCustomer.colLastName())}). */
    FIELD,
    /** A query over an arbitrary set of columns - a "plate" (e.g. {@code select(colA, colB)}). */
    PLATE,
    /** A stored procedure call. */
    PROCEDURE
}
