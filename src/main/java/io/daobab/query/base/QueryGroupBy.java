package io.daobab.query.base;

import io.daobab.model.Column;

import java.util.List;

/**
 * The {@code GROUP BY} fragment of a query, usually paired with aggregate functions in the select list and a
 * {@link QueryHaving having} clause:
 * <pre>{@code
 * db.select(tabOrder.colCustomerId(), sum(tabOrder.colAmount()))
 *   .groupBy(tabOrder.colCustomerId())
 *   .findMany();
 * }</pre>
 *
 * @param <Q> the concrete query type, returned for chaining
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings({"rawtypes", "UnusedReturnValue", "unused"})
public interface QueryGroupBy<Q extends Query> {

    /**
     * The columns the query groups by.
     */
    List<Column<?, ?, ?>> getGroupBy();

    /** The alias the query groups by, when grouping by an alias rather than columns. */
    String getGroupByAlias();

    /** Groups by the given alias. */
    Q groupBy(String alias);

    /** Groups by the given columns. */
    Q groupBy(Column<?, ?, ?>... columns);

}
