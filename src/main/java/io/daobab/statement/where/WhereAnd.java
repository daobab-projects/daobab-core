package io.daobab.statement.where;

import io.daobab.statement.where.base.Where;

/**
 * A where clause whose expressions are joined with {@code AND} - all of them have to hold. Nested
 * {@link WhereOr}/{@link WhereNot} clauses can be added to build an arbitrary boolean tree.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 * @see WhereOr
 * @see WhereNot
 */
public class WhereAnd extends Where<WhereAnd> {

    // ------ Contructors

    /**
     * Creates an empty {@code AND} clause.
     */
    public WhereAnd() {
        super();
    }

    /**
     * Creates an {@code AND} clause seeded with the given sub-clauses.
     *
     * @param whereAnds the sub-clauses to add
     */
    public WhereAnd(Where<?>... whereAnds) {
        for (Where<?> where : whereAnds) {
            temp(where);
        }
    }

    /**
     * Creates a new empty {@code AND} clause - a fluent alternative to the constructor.
     *
     * @return the new clause
     */
    public static WhereAnd and() {
        return new WhereAnd();
    }

    /**
     * Adds another (optimized) where clause to this {@code AND} clause.
     *
     * @param val the clause to add
     * @return this clause, for chaining
     */
    public final WhereAnd and(Where<?> val) {
        val.optimize();
        temp(val);
        return this;
    }

    /**
     * Starts a new empty {@code OR} clause.
     *
     * @return the new clause
     */
    public WhereOr or() {
        return new WhereOr();
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
     * @return the {@code AND} operator
     */
    @Override
    public String getRelationBetweenExpressions() {
        return AND;
    }

}
