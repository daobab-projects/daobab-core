package io.daobab.query.base;

import io.daobab.statement.condition.Limit;

/**
 * The {@code LIMIT}/paging fragment of a query. For example:
 * <pre>{@code
 * db.select(tabCustomer).limitBy(10).findMany();        // the first 10 rows
 * db.select(tabCustomer).limitBy(20, 10).findMany();    // 10 rows from offset 20
 * db.select(tabCustomer).page(2, 25).findMany();        // page 2, 25 rows per page
 * }</pre>
 *
 * @param <Q> the concrete query type, returned for chaining
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings({"unchecked", "rawtypes", "UnusedReturnValue", "unused"})
public interface QueryLimit<Q extends Query> {

    /**
     * Stores the limit on the query (implementation hook).
     */
    void setTempLimit(Limit limit);

    /** Sets the given {@link Limit}. */
    default Q setLimit(Limit limit) {
        setTempLimit(limit);
        return (Q) this;
    }

    /** Limits the result to the given {@link Limit}. */
    default Q limitBy(Limit limit) {
        this.setLimit(limit);
        return (Q) this;
    }

    /** Limits the result to at most {@code limit} rows. */
    default Q limitBy(int limit) {
        this.setLimit(new Limit(limit));
        return (Q) this;
    }

    /** Limits the result to {@code limit} rows starting at offset {@code from}. */
    default Q limitBy(int from, int limit) {
        this.setLimit(new Limit(from, limit));
        return (Q) this;
    }

    /** Limits the result to the given page ({@code pageId}, zero-based) of {@code pageSize} rows. */
    default Q page(int pageId, int pageSize) {
        this.setTempPage(pageId, pageSize);
        return (Q) this;
    }

    /** Stores a page as a {@link Limit} (offset {@code pageNo * elementsOnPage}). */
    default void setTempPage(int pageNo, int elementsOnPage) {
        Limit lim = new Limit(elementsOnPage * pageNo, elementsOnPage);
        setTempLimit(lim);
    }
}
