package io.daobab.statement.where;

import io.daobab.statement.where.base.Where;

/**
 * A where clause whose expressions are joined with {@code OR} - any of them has to hold. Nested
 * {@link WhereAnd}/{@link WhereNot} clauses can be added to build an arbitrary boolean tree.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 * @see WhereAnd
 * @see WhereNot
 */
public class WhereOr extends Where<WhereOr> {

    // ------ Contructors

    /**
     * Creates an empty {@code OR} clause.
     */
    public WhereOr() {
        super();
    }

    /**
     * Creates an {@code OR} clause seeded with the given sub-clauses.
     *
     * @param whereAnds the sub-clauses to add
     */
    public WhereOr(Where<?>... whereAnds) {
        for (Where<?> where : whereAnds) {
            temp(where);
        }
    }

    /**
     * Creates a new empty {@code OR} clause - a fluent alternative to the constructor.
     *
     * @return the new clause
     */
    public static WhereOr or() {
        return new WhereOr();
    }

    /**
     * Adds another (optimized) where clause to this {@code OR} clause.
     *
     * @param where the clause to add
     * @return this clause, for chaining
     */
    public WhereOr or(Where<?> where) {
        where.optimize();
        temp(where);
        return this;
    }

    /**
     * Starts a new empty {@code AND} clause.
     *
     * @return the new clause
     */
    public WhereAnd and() {
        return new WhereAnd();
    }

    /**
     * Starts a new empty {@code NOT} clause.
     *
     * @return the new clause
     */
    public WhereNot not() {
        return new WhereNot();
    }


    /**
     * {@inheritDoc}
     *
     * @return the {@code OR} operator
     */
    @Override
    public String getRelationBetweenExpressions() {
        return OR;
    }

}
