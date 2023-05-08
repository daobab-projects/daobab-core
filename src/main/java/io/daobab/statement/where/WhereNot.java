package io.daobab.statement.where;

import io.daobab.statement.where.base.Where;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class WhereNot extends Where<WhereNot> {

    // ------ Contructors
    public WhereNot() {
        super();
    }

    public static WhereNot not() {
        return new WhereNot();
    }

    public WhereAnd and() {
        return new WhereAnd();
    }

    public WhereOr or() {
        return new WhereOr();
    }

    public WhereNot or(Where<?> where) {
        where.optimize();
        temp(where);
        return this;
    }

    @Override
    public String getRelationBetweenExpressions() {
        return NOT;
    }

}
