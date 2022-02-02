package io.daobab.query.base;

import io.daobab.model.*;
import io.daobab.target.buffer.single.Entities;
import io.daobab.statement.inner.InnerQueryEntity;
import io.daobab.statement.inner.InnerQueryField;
import io.daobab.statement.where.WhereAnd;
import io.daobab.statement.where.WhereOr;
import io.daobab.statement.where.base.Where;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
@SuppressWarnings({"unchecked","rawtypes","UnusedReturnValue","unused"})
public interface QueryWhere<Q extends Query> {

    void setWhereWrapper(Where whereWrapper);

    Where getWhereWrapper();


    default Q where(Where wrapper) {
        setWhereWrapper(wrapper);
        return (Q) this;
    }


    default Q whereAnd(UnaryOperator<WhereAnd> condition) {
        WhereAnd and = new WhereAnd();
        and = condition.apply(and);
        setWhereWrapper(and);
        return (Q) this;
    }

    default Q whereOR(Supplier<WhereOr> wrapper) {
        setWhereWrapper(wrapper.get());
        return (Q) this;
    }

    default Q whereOr(UnaryOperator<WhereOr> condition) {
        WhereOr or = new WhereOr();
        or = condition.apply(or);
        setWhereWrapper(or);
        return (Q) this;
    }


//    default <F, R extends EntityRelation> Q where(Column<? extends Entity, F, R> col, InnerQueryField<? extends R, F> val) {
//        setWhereWrapper(new WhereAND().in(col, val));
//        return (Q) this;
//    }

//    default <E extends Entity, F, R extends EntityRelation> Q where(Column<E, F, R> column, F val) {
//        setWhereWrapper(new WhereAND().equal(column, val));
//        return (Q) this;
//    }
//
//    default <E extends Entity, F, R extends EntityRelation> Q where(Column<E, F, R> column, R val) {
//        setWhereWrapper(new WhereAND().equal(column, column.getValueOf(val)));
//        return (Q) this;
//    }

    default <E extends Entity, F, R extends EntityRelation> Q whereIfNotNull(Column<E, F, R> column, F val) {
        if (val != null) setWhereWrapper(new WhereAnd().equal(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereEqual(Column<E, F, R> column, F val) {
        setWhereWrapper(new WhereAnd().equal(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereNotEqual(Column<E, F, R> column, F val) {
        setWhereWrapper(new WhereAnd().notEqual(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereIsNull(Column<E, F, R> column) {
        setWhereWrapper(new WhereAnd().isNull(column));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereNotNull(Column<E, F, R> column) {
        setWhereWrapper(new WhereAnd().notNull(column));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereGreater(Column<E, F, R> column, F val) {
        setWhereWrapper(new WhereAnd().greater(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereGreaterOrEqual(Column<E, F, R> column, F val) {
        setWhereWrapper(new WhereAnd().greaterOrEqual(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereLess(Column<E, F, R> column, F val) {
        setWhereWrapper(new WhereAnd().less(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereLessOrEqual(Column<E, F, R> column, F val) {
        setWhereWrapper(new WhereAnd().lessOrEqual(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereLike(Column<E, F, R> column, F val) {
        setWhereWrapper(new WhereAnd().like(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereEqual(Column<E, F, R> column, R val) {
        setWhereWrapper(new WhereAnd().equal(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereNotEqual(Column<E, F, R> column, R val) {
        setWhereWrapper(new WhereAnd().notEqual(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereGreater(Column<E, F, R> column, R val) {
        setWhereWrapper(new WhereAnd().greater(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereGreaterOrEqual(Column<E, F, R> column, R val) {
        setWhereWrapper(new WhereAnd().greaterOrEqual(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereLess(Column<E, F, R> column, R val) {
        setWhereWrapper(new WhereAnd().less(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereLessOrEqual(Column<E, F, R> column, R val) {
        setWhereWrapper(new WhereAnd().lessOrEqual(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereIn(Column<E, F, R> column, F... val) {
        setWhereWrapper(new WhereAnd().in(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereNotIn(Column<E, F, R> column, F... val) {
        setWhereWrapper(new WhereAnd().notIn(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereInFields(Column<E, F, R> column, Collection<F> val) {
        setWhereWrapper(new WhereAnd().inFields(column, val));
        return (Q) this;
    }
    default <E extends Entity, F, R extends EntityRelation> Q whereIn(Column<E, F, R> column, Entities<? extends R> val) {
        setWhereWrapper(new WhereAnd().in(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereNotInFields(Column<E, F, R> column, Collection<F> val) {
        setWhereWrapper(new WhereAnd().notInFields(column, val));
        return (Q) this;
    }
    default <E extends Entity, F, R extends EntityRelation> Q whereNotIn(Column<E, F, R> column, Entities<? extends R> val) {
        setWhereWrapper(new WhereAnd().notIn(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereIn(Column<E, F, R> column, R... val) {
        setWhereWrapper(new WhereAnd().in(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereNotIn(Column<E, F, R> column, R... val) {
        setWhereWrapper(new WhereAnd().notIn(column, val));
        return (Q) this;
    }

//    default <F, R extends EntityRelation> Q where(Column<?, F, R> column, Operator operator, Entities<? extends R> val) {
//        setWhereWrapper(new WhereAND().and(column, operator, val));
//        return (Q) this;
//    }

//    default <F> Q where(Column<?, F, ?> column, Column<?, F, ?> column2) {
//        setWhereWrapper(new WhereAND().equal(column, column2));
//        return (Q) this;
//    }

    default <F> Q whereEqual(Column<?, F, ?> column, Column<?, F, ?> column2) {
        setWhereWrapper(new WhereAnd().equal(column, column2));
        return (Q) this;
    }

    default <F> Q whereGreater(Column<?, F, ?> column, Column<?, F, ?> column2) {
        setWhereWrapper(new WhereAnd().greater(column, column2));
        return (Q) this;
    }

    default <F> Q whereGreaterOrEqual(Column<?, F, ?> column, Column<?, F, ?> column2) {
        setWhereWrapper(new WhereAnd().greaterOrEqual(column, column2));
        return (Q) this;
    }

    default <F> Q whereLess(Column<?, F, ?> column, Column<?, F, ?> column2) {
        setWhereWrapper(new WhereAnd().less(column, column2));
        return (Q) this;
    }

    default <F> Q whereLessOrEqual(Column<?, F, ?> column, Column<?, F, ?> column2) {
        setWhereWrapper(new WhereAnd().lessOrEqual(column, column2));
        return (Q) this;
    }

    default <F> Q whereBetween(Column<?, F, ?> column, F valueFrom, F valueTo) {
        setWhereWrapper(new WhereAnd().between(column, valueFrom, valueTo));
        return (Q) this;
    }

    default <F, R extends EntityRelation> Q whereBetween(Column<?, F, R> column, R valueFrom, R valueTo) {
        setWhereWrapper(new WhereAnd().between(column, valueFrom, valueTo));
        return (Q) this;
    }


//    /**
//     * @param column-   representation of table column or class field
//     * @param operator- logical relation
//     * @param column2-  representation of table column or class field
//     * @param <F>       value type
//     * @return WhereAND
//     */
//    default <F> Q whereColumns(Column<?, F, ?> column, Operator operator, Column<?, F, ?> column2) {
//        setWhereWrapper(new WhereAND().and(column, operator, column2));
//        return (Q) this;
//    }



    default <E extends Entity, F, R extends EntityRelation> Q whereEqual(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        setWhereWrapper(new WhereAnd().equal(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereGreater(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        setWhereWrapper(new WhereAnd().greater(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereGreaterOrEqual(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        setWhereWrapper(new WhereAnd().greaterOrEqual(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereLess(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        setWhereWrapper(new WhereAnd().less(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereLessOrEqual(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        setWhereWrapper(new WhereAnd().lessOrEqual(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereNotEqual(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        setWhereWrapper(new WhereAnd().notEqual(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereLike(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        setWhereWrapper(new WhereAnd().like(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereNotLike(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        setWhereWrapper(new WhereAnd().notLike(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereIn(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        setWhereWrapper(new WhereAnd().in(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereNotIn(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        setWhereWrapper(new WhereAnd().notIn(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereEqual(Field<E, F, R> column, InnerQueryField<? extends R, F> val) {
        setWhereWrapper(new WhereAnd().equal(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereGreater(Field<E, F, R> column, InnerQueryField<? extends R, F> val) {
        setWhereWrapper(new WhereAnd().greater(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereGreaterOrEqual(Field<E, F, R> column, InnerQueryField<? extends R, F> val) {
        setWhereWrapper(new WhereAnd().greaterOrEqual(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereLess(Field<E, F, R> column, InnerQueryField<? extends R, F> val) {
        setWhereWrapper(new WhereAnd().less(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereLessOrEqual(Field<E, F, R> column, InnerQueryField<? extends R, F> val) {
        setWhereWrapper(new WhereAnd().lessOrEqual(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereNotEqual(Field<E, F, R> column, InnerQueryField<? extends R, F> val) {
        setWhereWrapper(new WhereAnd().notEqual(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereLike(Field<E, F, R> column, InnerQueryField<? extends R, F> val) {
        setWhereWrapper(new WhereAnd().like(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereNotLike(Field<E, F, R> column, InnerQueryField<? extends R, F> val) {
        setWhereWrapper(new WhereAnd().notLike(column, val));
        return (Q) this;
    }


    default <E extends Entity, F, R extends EntityRelation> Q whereIn(Field<E, F, R> column, InnerQueryField<? extends R, F> val) {
        setWhereWrapper(new WhereAnd().in(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q whereNotIn(Field<E, F, R> column, InnerQueryField<? extends R, F> val) {
        setWhereWrapper(new WhereAnd().notIn(column, val));
        return (Q) this;
    }

    default <K extends Composite> Q whereEqual(CompositeColumns key, K val) {
        setWhereWrapper(new WhereAnd().equal(key, val));
        return (Q) this;
    }

    default <K extends Composite> Q whereNotEqual(CompositeColumns key, K val) {
        setWhereWrapper(new WhereAnd().equal(key, val));
        return (Q) this;
    }
}
