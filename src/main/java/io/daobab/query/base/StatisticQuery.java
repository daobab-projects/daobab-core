package io.daobab.query.base;

/**
 * The read-only metadata of a query: its identifier, the queried entity, the SQL actually sent and the
 * {@link QueryType}. Used by the statistics/diagnostics facilities.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface StatisticQuery {

    /**
     * The query identifier (alias).
     */
    String getIdentifier();

    /** Sets the query identifier (alias). */
    void setIdentifier(String identifier);

    /** The name of the queried entity. */
    String getEntityName();

    /** The SQL that was actually sent to the target, once executed. */
    String getSentQuery();

    /** The kind of result this query produces. */
    QueryType getQueryType();
}
