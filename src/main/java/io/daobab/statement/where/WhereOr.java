package io.daobab.statement.where;

import io.daobab.statement.where.base.Where;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public class WhereOr extends Where<WhereOr> {

    // ------ Contructors
    public WhereOr() {
        super();
    }

    public WhereOr(Where<?>... whereAnds) {
        for (Where<?> where : whereAnds) {
            temp(where);
        }
    }

//    public WhereOr or(UnaryOperator<WhereOr> condition) {
//        WhereOr or = new WhereOr();
//        or = condition.apply(or);
//        return or;
//    }

    public static WhereOr or() {
        return new WhereOr();
    }

    public WhereOr or(Where<?> where) {
        where.optimize();
        temp(where);
        return this;
    }

    public WhereAnd and() {
        return new WhereAnd();
    }

    public WhereNot not() {
        return new WhereNot();
    }


    @Override
    public String getRelationBetweenExpressions() {
        return OR;
    }

}
