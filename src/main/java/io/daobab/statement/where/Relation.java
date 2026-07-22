package io.daobab.statement.where;

/**
 * The logical operator joining the expressions of a where clause.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 * @see WhereAnd
 * @see WhereOr
 * @see WhereNot
 */
public enum Relation {
    /**
     * Disjunction: any of the joined expressions has to hold.
     */
    OR,
    /**
     * Conjunction: all of the joined expressions have to hold.
     */
    AND,
    /**
     * Negation: none of the joined expressions may hold.
     */
    NOT
}
