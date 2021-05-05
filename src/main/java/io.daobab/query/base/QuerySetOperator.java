package io.daobab.query.base;

import io.daobab.statement.condition.SetOperator;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
@SuppressWarnings({"unchecked", "rawtypes", "UnusedReturnValue", "unused"})
public interface QuerySetOperator<Q extends Query> {

    void addSetOperator(SetOperator union);


    default Q union(Query<?, ?> query) {
        this.addSetOperator(new SetOperator(SetOperator.UNION, query));
        return (Q) this;
    }


    default Q unionAll(Query<?, ?> query) {
        this.addSetOperator(new SetOperator(SetOperator.UNION_ALL, query));
        return (Q) this;
    }


    default Q except(Query<?, ?> query) {
        this.addSetOperator(new SetOperator(SetOperator.EXCEPT, query));
        return (Q) this;
    }


    default Q exceptAll(Query<?, ?> query) {
        this.addSetOperator(new SetOperator(SetOperator.EXCEPT_ALL, query));
        return (Q) this;
    }


    default Q intersect(Query<?, ?> query) {
        this.addSetOperator(new SetOperator(SetOperator.INTERSECT, query));
        return (Q) this;
    }


    default Q minus(Query<?, ?> query) {
        this.addSetOperator(new SetOperator(SetOperator.MINUS, query));
        return (Q) this;
    }
}
