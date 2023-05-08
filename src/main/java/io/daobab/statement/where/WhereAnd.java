package io.daobab.statement.where;

import io.daobab.statement.where.base.Where;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class WhereAnd extends Where<WhereAnd> {

    // ------ Contructors
    public WhereAnd() {
        super();
    }

    public WhereAnd(Where<?>... whereAnds) {
        for (Where<?> where : whereAnds) {
            temp(where);
        }
    }

    public static WhereAnd and() {
        return new WhereAnd();
    }

    /**
     * Add another Where clause
     *
     * @param val - value
     * @return WhereAND
     */
    public final WhereAnd and(Where<?> val) {
        val.optimize();
        temp(val);
        return this;
    }

    public WhereOr or() {
        return new WhereOr();
    }

    public WhereNot not() {
        return new WhereNot();
    }

    @Override
    public String getRelationBetweenExpressions() {
        return AND;
    }

}
