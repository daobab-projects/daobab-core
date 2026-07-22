package io.daobab.query.base;

import io.daobab.statement.condition.SetOperator;

/**
 * The set operators combining this query with another one: {@code UNION}, {@code UNION ALL}, {@code EXCEPT},
 * {@code INTERSECT} and {@code MINUS}. For example:
 * <pre>{@code
 * db.select(tabActiveCustomer.colEmail())
 *   .union(db.select(tabProspect.colEmail()))
 *   .findMany();
 * }</pre>
 *
 * @param <Q> the concrete query type, returned for chaining
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings({"unchecked", "rawtypes", "UnusedReturnValue", "unused"})
public interface QuerySetOperator<Q extends Query> {

    /**
     * Adds a set operator combining this query with another.
     */
    void addSetOperator(SetOperator union);

    /** Combines this query with {@code query} using {@code UNION} (distinct rows). */
    default Q union(Query<?, ?, ?> query) {
        this.addSetOperator(new SetOperator(SetOperator.UNION, query));
        return (Q) this;
    }

    /** Combines this query with {@code query} using {@code UNION ALL} (keeps duplicates). */
    default Q unionAll(Query<?, ?, ?> query) {
        this.addSetOperator(new SetOperator(SetOperator.UNION_ALL, query));
        return (Q) this;
    }

    /** Combines this query with {@code query} using {@code EXCEPT} (rows here but not in {@code query}). */
    default Q except(Query<?, ?, ?> query) {
        this.addSetOperator(new SetOperator(SetOperator.EXCEPT, query));
        return (Q) this;
    }

    /** Combines this query with {@code query} using {@code EXCEPT ALL} (keeps duplicates). */
    default Q exceptAll(Query<?, ?, ?> query) {
        this.addSetOperator(new SetOperator(SetOperator.EXCEPT_ALL, query));
        return (Q) this;
    }

    /** Combines this query with {@code query} using {@code INTERSECT} (rows present in both). */
    default Q intersect(Query<?, ?, ?> query) {
        this.addSetOperator(new SetOperator(SetOperator.INTERSECT, query));
        return (Q) this;
    }

    /** Combines this query with {@code query} using {@code MINUS} (the Oracle synonym of {@code EXCEPT}). */
    default Q minus(Query<?, ?, ?> query) {
        this.addSetOperator(new SetOperator(SetOperator.MINUS, query));
        return (Q) this;
    }
}
