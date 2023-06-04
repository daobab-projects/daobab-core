package io.daobab.target.database.query.base;

import io.daobab.model.*;
import io.daobab.query.base.QueryWhere;
import io.daobab.statement.where.WhereAnd;
import io.daobab.statement.where.WhereNot;
import io.daobab.statement.where.WhereOr;
import io.daobab.target.database.query.DataBaseQueryBase;
import io.daobab.target.database.query.frozen.DaoParam;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@SuppressWarnings({"rawtypes", "unchecked"})
public interface DataBaseQueryWhere<Q extends DataBaseQueryBase> extends QueryWhere<Q> {


//    default Q whereAnd(UnaryOperator<DataBaseWhereAnd> condition) {
//        DataBaseWhereAnd and = new DataBaseWhereAnd();
//        and = condition.apply(and);
//        setWhereWrapper(and);
//        return (Q) this;
//    }

    default Q whereOR(Supplier<WhereOr> wrapper) {
        setWhereWrapper(wrapper.get());
        return (Q) this;
    }

    @SuppressWarnings("java:S1845")
    default Q whereOr(UnaryOperator<WhereOr> condition) {
        WhereOr or = new WhereOr();
        or = condition.apply(or);
        setWhereWrapper(or);
        return (Q) this;
    }

    default Q whereNOT(Supplier<WhereNot> wrapper) {
        setWhereWrapper(wrapper.get());
        return (Q) this;
    }

    @SuppressWarnings("java:S1845")
    default Q whereNot(UnaryOperator<WhereNot> condition) {
        WhereNot not = new WhereNot();
        not = condition.apply(not);
        setWhereWrapper(not);
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereEqual(Column<E, F, R> column, DaoParam val) {
        setWhereWrapper(new DataBaseWhereAnd().equal(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereNotEqual(Column<E, F, R> column, DaoParam val) {
        setWhereWrapper(new DataBaseWhereAnd().notEqual(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereGreater(Column<E, F, R> column, DaoParam val) {
        setWhereWrapper(new DataBaseWhereAnd().greater(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereGreaterOrEqual(Column<E, F, R> column, DaoParam val) {
        setWhereWrapper(new DataBaseWhereAnd().greaterOrEqual(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereLess(Column<E, F, R> column, DaoParam val) {
        setWhereWrapper(new DataBaseWhereAnd().less(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereLessOrEqual(Column<E, F, R> column, DaoParam val) {
        setWhereWrapper(new DataBaseWhereAnd().lessOrEqual(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereLike(Column<E, F, R> column, DaoParam val) {
        setWhereWrapper(new DataBaseWhereAnd().like(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereInCollectionParam(Column<E, F, R> column, DaoParam val) {
        setWhereWrapper(new DataBaseWhereAnd().inFieldsParam(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereNotInCollectionParam(Column<E, F, R> column, DaoParam val) {
        setWhereWrapper(new DataBaseWhereAnd().notInFieldsParam(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereInFieldsParam(Column<E, F, R> column, DaoParam val) {
        setWhereWrapper(new DataBaseWhereAnd().inFieldsParam(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereNotInFieldsParam(Column<E, F, R> column, DaoParam val) {
        setWhereWrapper(new DataBaseWhereAnd().notInFieldsParam(column, val));
        return (Q) this;
    }

    default <F> Q whereBetween(Column<?, F, ?> column, DaoParam valueFrom, DaoParam valueTo) {
        setWhereWrapper(new DataBaseWhereAnd().between(column, valueFrom, valueTo));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereIn(Column<E, F, R> column, DaoParam val) {
        setWhereWrapper(new DataBaseWhereAnd().in(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereNotIn(Column<E, F, R> column, DaoParam val) {
        setWhereWrapper(new DataBaseWhereAnd().notIn(column, val));
        return (Q) this;
    }


}
