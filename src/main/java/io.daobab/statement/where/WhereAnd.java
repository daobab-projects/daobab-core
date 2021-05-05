package io.daobab.statement.where;

import io.daobab.statement.where.base.Where;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class WhereAnd extends Where<WhereAnd> {

    // ------ Contructors
    public WhereAnd() {
        super();
    }

    /**
     * Add another Where clause
     *
     * @param val - value
     * @return WhereAND
     */
    public final WhereAnd and(Where val) {
        val.optimize();
        temp(val);
        return this;
    }

    public WhereAnd(Where... whereAnds) {
        for (Where where : whereAnds) {
            temp(where);
        }
    }


    @Override
    public String getRelationBetweenExpressions() {
        return AND;
    }

}
