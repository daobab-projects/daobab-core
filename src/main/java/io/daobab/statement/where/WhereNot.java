package io.daobab.statement.where;

import io.daobab.statement.where.base.Where;

/**
 * A where clause negating its expressions with {@code NOT}. Nested {@link WhereAnd}/{@link WhereOr} clauses
 * can be added to build an arbitrary boolean tree.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 * @see WhereAnd
 * @see WhereOr
 */
public class WhereNot extends Where<WhereNot> {

    // ------ Contructors

    /**
     * Creates an empty {@code NOT} clause.
     */
    public WhereNot() {
        super();
    }

    /**
     * Creates a new empty {@code NOT} clause - a fluent alternative to the constructor.
     *
     * @return the new clause
     */
    public static WhereNot not() {
        return new WhereNot();
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
     * Starts a new empty {@code OR} clause.
     *
     * @return the new clause
     */
    public WhereOr or() {
        return new WhereOr();
    }

    /**
     * Adds another (optimized) where clause under this {@code NOT} clause.
     *
     * @param where the clause to add
     * @return this clause, for chaining
     */
    public WhereNot or(Where<?> where) {
        where.optimize();
        temp(where);
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @return the {@code NOT} operator
     */
    @Override
    public String getRelationBetweenExpressions() {
        return NOT;
    }

}
