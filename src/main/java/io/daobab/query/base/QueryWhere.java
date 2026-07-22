package io.daobab.query.base;

import io.daobab.model.*;
import io.daobab.statement.inner.InnerQueryEntity;
import io.daobab.statement.inner.InnerQueryFieldsProvider;
import io.daobab.statement.where.WhereAnd;
import io.daobab.statement.where.WhereNot;
import io.daobab.statement.where.WhereOr;
import io.daobab.statement.where.base.Where;
import io.daobab.target.buffer.single.Entities;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * The {@code WHERE} fragment of a query.
 * <p>
 * Each {@code whereXxx} shortcut <b>replaces</b> the whole where clause with one condition - they are
 * conveniences for a query filtered by a single predicate. To combine several conditions, build the clause
 * explicitly with {@link #where(Where)} (or {@link #whereAnd(UnaryOperator)} / {@link #whereOr(UnaryOperator)}):
 * <pre>{@code
 * // single-condition shortcut
 * db.select(tabBook).whereEqual(tabBook.colAuthorId(), 2).findMany();
 *
 * // several conditions - build the clause
 * db.select(tabBook)
 *   .where(and()
 *       .equal(tabBook.colActive(), true)
 *       .greater(tabBook.colPrice(), 20))
 *   .findMany();
 *
 * // ...or with a lambda
 * db.select(tabBook)
 *   .whereAnd(w -> w.equal(tabBook.colActive(), true).greater(tabBook.colPrice(), 20))
 *   .findMany();
 * }</pre>
 * The value comes in several shapes: a plain field value ({@code F}), a related entity ({@code R}), another
 * column, a collection or an inner (sub)query.
 *
 * @param <Q> the concrete query type, returned for chaining
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings({"unchecked", "rawtypes", "UnusedReturnValue", "unused"})
public interface QueryWhere<Q extends Query> {

    /**
     * The where clause, or {@code null} when there is none.
     */
    Where getWhereWrapper();

    /** Sets the where clause. */
    void setWhereWrapper(Where whereWrapper);

    /** Sets the where clause to the given one. */
    default Q where(Where wrapper) {
        setWhereWrapper(wrapper);
        return (Q) this;
    }

    /** Sets the where clause to an {@code AND} group built by the lambda. */
    default Q whereAnd(UnaryOperator<WhereAnd> condition) {
        WhereAnd and = new WhereAnd();
        and = condition.apply(and);
        setWhereWrapper(and);
        return (Q) this;
    }

    /** Sets the where clause to the supplied {@code OR} group. */
    default Q whereOR(Supplier<WhereOr> wrapper) {
        setWhereWrapper(wrapper.get());
        return (Q) this;
    }

    /** Sets the where clause to an {@code OR} group built by the lambda. */
    @SuppressWarnings("java:S1845")
    default Q whereOr(UnaryOperator<WhereOr> condition) {
        WhereOr or = new WhereOr();
        or = condition.apply(or);
        setWhereWrapper(or);
        return (Q) this;
    }

    /** Sets the where clause to the supplied {@code NOT} group. */
    default Q whereNOT(Supplier<WhereNot> wrapper) {
        setWhereWrapper(wrapper.get());
        return (Q) this;
    }

    /** Sets the where clause to a {@code NOT} group built by the lambda. */
    @SuppressWarnings("java:S1845")
    default Q whereNot(UnaryOperator<WhereNot> condition) {
        WhereNot not = new WhereNot();
        not = condition.apply(not);
        setWhereWrapper(not);
        return (Q) this;
    }

    /** Sets a {@code column = value} condition only when {@code val} is not {@code null}. */
    default <E extends Entity, F, R extends RelatedTo> Q whereIfNotNull(Column<E, F, R> column, F val) {
        if (val != null) setWhereWrapper(new WhereAnd().equal(column, val));
        return (Q) this;
    }

    /** Sets a {@code column = value} condition. */
    default <E extends Entity, F, R extends RelatedTo> Q whereEqual(Column<E, F, R> column, F val) {
        setWhereWrapper(new WhereAnd().equal(column, val));
        return (Q) this;
    }

    /** Sets a {@code column <> value} condition. */
    default <E extends Entity, F, R extends RelatedTo> Q whereNotEqual(Column<E, F, R> column, F val) {
        setWhereWrapper(new WhereAnd().notEqual(column, val));
        return (Q) this;
    }

    /** Sets a {@code column IS NULL} condition. */
    default <E extends Entity, F, R extends RelatedTo> Q whereIsNull(Column<E, F, R> column) {
        setWhereWrapper(new WhereAnd().isNull(column));
        return (Q) this;
    }

    /** Sets a {@code column IS NOT NULL} condition. */
    default <E extends Entity, F, R extends RelatedTo> Q whereNotNull(Column<E, F, R> column) {
        setWhereWrapper(new WhereAnd().notNull(column));
        return (Q) this;
    }

    /** Sets a {@code column > value} condition. */
    default <E extends Entity, F, R extends RelatedTo> Q whereGreater(Column<E, F, R> column, F val) {
        setWhereWrapper(new WhereAnd().greater(column, val));
        return (Q) this;
    }

    /** Sets a {@code column >= value} condition. */
    default <E extends Entity, F, R extends RelatedTo> Q whereGreaterOrEqual(Column<E, F, R> column, F val) {
        setWhereWrapper(new WhereAnd().greaterOrEqual(column, val));
        return (Q) this;
    }

    /** Sets a {@code column < value} condition. */
    default <E extends Entity, F, R extends RelatedTo> Q whereLess(Column<E, F, R> column, F val) {
        setWhereWrapper(new WhereAnd().less(column, val));
        return (Q) this;
    }

    /** Sets a {@code column <= value} condition. */
    default <E extends Entity, F, R extends RelatedTo> Q whereLessOrEqual(Column<E, F, R> column, F val) {
        setWhereWrapper(new WhereAnd().lessOrEqual(column, val));
        return (Q) this;
    }

    /** Sets a {@code column LIKE value} condition. */
    default <E extends Entity, F, R extends RelatedTo> Q whereLike(Column<E, F, R> column, F val) {
        setWhereWrapper(new WhereAnd().like(column, val));
        return (Q) this;
    }

    /** Sets a {@code column = value} condition, the value taken from the related entity. */
    default <E extends Entity, F, R extends RelatedTo> Q whereEqual(Column<E, F, R> column, R val) {
        setWhereWrapper(new WhereAnd().equal(column, val));
        return (Q) this;
    }

    /** Sets a {@code column <> value} condition, the value taken from the related entity. */
    default <E extends Entity, F, R extends RelatedTo> Q whereNotEqual(Column<E, F, R> column, R val) {
        setWhereWrapper(new WhereAnd().notEqual(column, val));
        return (Q) this;
    }

    /** Sets a {@code column > value} condition, the value taken from the related entity. */
    default <E extends Entity, F, R extends RelatedTo> Q whereGreater(Column<E, F, R> column, R val) {
        setWhereWrapper(new WhereAnd().greater(column, val));
        return (Q) this;
    }

    /** Sets a {@code column >= value} condition, the value taken from the related entity. */
    default <E extends Entity, F, R extends RelatedTo> Q whereGreaterOrEqual(Column<E, F, R> column, R val) {
        setWhereWrapper(new WhereAnd().greaterOrEqual(column, val));
        return (Q) this;
    }

    /** Sets a {@code column < value} condition, the value taken from the related entity. */
    default <E extends Entity, F, R extends RelatedTo> Q whereLess(Column<E, F, R> column, R val) {
        setWhereWrapper(new WhereAnd().less(column, val));
        return (Q) this;
    }

    /** Sets a {@code column <= value} condition, the value taken from the related entity. */
    default <E extends Entity, F, R extends RelatedTo> Q whereLessOrEqual(Column<E, F, R> column, R val) {
        setWhereWrapper(new WhereAnd().lessOrEqual(column, val));
        return (Q) this;
    }

    /** Sets a {@code column IN (values)} condition. */
    default <E extends Entity, F, R extends RelatedTo> Q whereIn(Column<E, F, R> column, F... val) {
        setWhereWrapper(new WhereAnd().in(column, val));
        return (Q) this;
    }

    /** Sets a {@code column NOT IN (values)} condition. */
    default <E extends Entity, F, R extends RelatedTo> Q whereNotIn(Column<E, F, R> column, F... val) {
        setWhereWrapper(new WhereAnd().notIn(column, val));
        return (Q) this;
    }


    /** Sets a {@code column IN (values)} condition over a collection of field values. */
    default <E extends Entity, F, R extends RelatedTo> Q whereInCollection(Column<E, F, R> column, Collection<F> val) {
        setWhereWrapper(new WhereAnd().inFields(column, val));
        return (Q) this;
    }

    /** Sets a {@code column NOT IN (values)} condition over a collection of field values. */
    default <E extends Entity, F, R extends RelatedTo> Q whereNotInCollection(Column<E, F, R> column, Collection<F> val) {
        setWhereWrapper(new WhereAnd().notInFields(column, val));
        return (Q) this;
    }

    /** Sets a {@code column IN (values)} condition over a collection of field values. */
    default <E extends Entity, F, R extends RelatedTo> Q whereInFields(Column<E, F, R> column, Collection<F> val) {
        setWhereWrapper(new WhereAnd().inFields(column, val));
        return (Q) this;
    }

    /** Sets a {@code column IN (...)} condition over a set of entities. */
    default <E extends Entity, F, R extends RelatedTo> Q whereIn(Column<E, F, R> column, Entities<? extends R> val) {
        setWhereWrapper(new WhereAnd().in(column, val));
        return (Q) this;
    }

    /** Sets a {@code column NOT IN (values)} condition over a collection of field values. */
    default <E extends Entity, F, R extends RelatedTo> Q whereNotInFields(Column<E, F, R> column, Collection<F> val) {
        setWhereWrapper(new WhereAnd().notInFields(column, val));
        return (Q) this;
    }

    /** Sets a {@code column NOT IN (...)} condition over a set of entities. */
    default <E extends Entity, F, R extends RelatedTo> Q whereNotIn(Column<E, F, R> column, Entities<? extends R> val) {
        setWhereWrapper(new WhereAnd().notIn(column, val));
        return (Q) this;
    }

    /** Sets a {@code column IN (...)} condition over the values of the related entities. */
    default <E extends Entity, F, R extends RelatedTo> Q whereIn(Column<E, F, R> column, R... val) {
        setWhereWrapper(new WhereAnd().in(column, val));
        return (Q) this;
    }

    /** Sets a {@code column NOT IN (...)} condition over the values of the related entities. */
    default <E extends Entity, F, R extends RelatedTo> Q whereNotIn(Column<E, F, R> column, R... val) {
        setWhereWrapper(new WhereAnd().notIn(column, val));
        return (Q) this;
    }

    /** Sets a {@code column = column2} condition comparing two columns. */
    default <F> Q whereEqual(Column<?, F, ?> column, Column<?, F, ?> column2) {
        setWhereWrapper(new WhereAnd().equal(column, column2));
        return (Q) this;
    }

    /** Sets a {@code column > column2} condition comparing two columns. */
    default <F> Q whereGreater(Column<?, F, ?> column, Column<?, F, ?> column2) {
        setWhereWrapper(new WhereAnd().greater(column, column2));
        return (Q) this;
    }

    /** Sets a {@code column >= column2} condition comparing two columns. */
    default <F> Q whereGreaterOrEqual(Column<?, F, ?> column, Column<?, F, ?> column2) {
        setWhereWrapper(new WhereAnd().greaterOrEqual(column, column2));
        return (Q) this;
    }

    /** Sets a {@code column < column2} condition comparing two columns. */
    default <F> Q whereLess(Column<?, F, ?> column, Column<?, F, ?> column2) {
        setWhereWrapper(new WhereAnd().less(column, column2));
        return (Q) this;
    }

    /** Sets a {@code column <= column2} condition comparing two columns. */
    default <F> Q whereLessOrEqual(Column<?, F, ?> column, Column<?, F, ?> column2) {
        setWhereWrapper(new WhereAnd().lessOrEqual(column, column2));
        return (Q) this;
    }

    /** Sets a strictly-between condition (greater than {@code valueFrom} and less than {@code valueTo}). */
    default <F> Q whereBetween(Column<?, F, ?> column, F valueFrom, F valueTo) {
        setWhereWrapper(new WhereAnd().between(column, valueFrom, valueTo));
        return (Q) this;
    }

    /** Sets a strictly-between condition over the values of the related entities. */
    default <F, R extends RelatedTo> Q whereBetween(Column<?, F, R> column, R valueFrom, R valueTo) {
        setWhereWrapper(new WhereAnd().between(column, valueFrom, valueTo));
        return (Q) this;
    }

    /** Sets a {@code column = (subquery)} condition, the entity subquery projected to the column. */
    default <E extends Entity, F, R extends RelatedTo> Q whereEqual(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        setWhereWrapper(new WhereAnd().equal(column, val));
        return (Q) this;
    }

    /** Sets a {@code column > (subquery)} condition, the entity subquery projected to the column. */
    default <E extends Entity, F, R extends RelatedTo> Q whereGreater(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        setWhereWrapper(new WhereAnd().greater(column, val));
        return (Q) this;
    }

    /** Sets a {@code column >= (subquery)} condition, the entity subquery projected to the column. */
    default <E extends Entity, F, R extends RelatedTo> Q whereGreaterOrEqual(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        setWhereWrapper(new WhereAnd().greaterOrEqual(column, val));
        return (Q) this;
    }

    /** Sets a {@code column < (subquery)} condition, the entity subquery projected to the column. */
    default <E extends Entity, F, R extends RelatedTo> Q whereLess(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        setWhereWrapper(new WhereAnd().less(column, val));
        return (Q) this;
    }

    /** Sets a {@code column <= (subquery)} condition, the entity subquery projected to the column. */
    default <E extends Entity, F, R extends RelatedTo> Q whereLessOrEqual(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        setWhereWrapper(new WhereAnd().lessOrEqual(column, val));
        return (Q) this;
    }

    /** Sets a {@code column <> (subquery)} condition, the entity subquery projected to the column. */
    default <E extends Entity, F, R extends RelatedTo> Q whereNotEqual(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        setWhereWrapper(new WhereAnd().notEqual(column, val));
        return (Q) this;
    }

    /** Sets a {@code column LIKE (subquery)} condition, the entity subquery projected to the column. */
    default <E extends Entity, F, R extends RelatedTo> Q whereLike(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        setWhereWrapper(new WhereAnd().like(column, val));
        return (Q) this;
    }

    /** Sets a {@code column NOT LIKE (subquery)} condition, the entity subquery projected to the column. */
    default <E extends Entity, F, R extends RelatedTo> Q whereNotLike(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        setWhereWrapper(new WhereAnd().notLike(column, val));
        return (Q) this;
    }

    /** Sets a {@code column IN (subquery)} condition, the entity subquery projected to the column. */
    default <E extends Entity, F, R extends RelatedTo> Q whereIn(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        setWhereWrapper(new WhereAnd().in(column, val));
        return (Q) this;
    }

    /** Sets a {@code column NOT IN (subquery)} condition, the entity subquery projected to the column. */
    default <E extends Entity, F, R extends RelatedTo> Q whereNotIn(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        setWhereWrapper(new WhereAnd().notIn(column, val));
        return (Q) this;
    }

    /** Sets a {@code column = (subquery)} condition, the subquery returning a single field. */
    default <E extends Entity, F, R extends RelatedTo> Q whereEqual(Field<E, F, R> column, InnerQueryFieldsProvider<? extends R, F> val) {
        setWhereWrapper(new WhereAnd().equal(column, val));
        return (Q) this;
    }

    /** Sets a {@code column > (subquery)} condition, the subquery returning a single field. */
    default <E extends Entity, F, R extends RelatedTo> Q whereGreater(Field<E, F, R> column, InnerQueryFieldsProvider<? extends R, F> val) {
        setWhereWrapper(new WhereAnd().greater(column, val));
        return (Q) this;
    }

    /** Sets a {@code column >= (subquery)} condition, the subquery returning a single field. */
    default <E extends Entity, F, R extends RelatedTo> Q whereGreaterOrEqual(Field<E, F, R> column, InnerQueryFieldsProvider<? extends R, F> val) {
        setWhereWrapper(new WhereAnd().greaterOrEqual(column, val));
        return (Q) this;
    }

    /** Sets a {@code column < (subquery)} condition, the subquery returning a single field. */
    default <E extends Entity, F, R extends RelatedTo> Q whereLess(Field<E, F, R> column, InnerQueryFieldsProvider<? extends R, F> val) {
        setWhereWrapper(new WhereAnd().less(column, val));
        return (Q) this;
    }

    /** Sets a {@code column <= (subquery)} condition, the subquery returning a single field. */
    default <E extends Entity, F, R extends RelatedTo> Q whereLessOrEqual(Field<E, F, R> column, InnerQueryFieldsProvider<? extends R, F> val) {
        setWhereWrapper(new WhereAnd().lessOrEqual(column, val));
        return (Q) this;
    }

    /** Sets a {@code column <> (subquery)} condition, the subquery returning a single field. */
    default <E extends Entity, F, R extends RelatedTo> Q whereNotEqual(Field<E, F, R> column, InnerQueryFieldsProvider<? extends R, F> val) {
        setWhereWrapper(new WhereAnd().notEqual(column, val));
        return (Q) this;
    }

    /** Sets a {@code column LIKE (subquery)} condition, the subquery returning a single field. */
    default <E extends Entity, F, R extends RelatedTo> Q whereLike(Field<E, F, R> column, InnerQueryFieldsProvider<? extends R, F> val) {
        setWhereWrapper(new WhereAnd().like(column, val));
        return (Q) this;
    }

    /** Sets a {@code column NOT LIKE (subquery)} condition, the subquery returning a single field. */
    default <E extends Entity, F, R extends RelatedTo> Q whereNotLike(Field<E, F, R> column, InnerQueryFieldsProvider<? extends R, F> val) {
        setWhereWrapper(new WhereAnd().notLike(column, val));
        return (Q) this;
    }

    /** Sets a {@code column IN (subquery)} condition, the subquery returning a single field. */
    default <E extends Entity, F, R extends RelatedTo> Q whereIn(Field<E, F, R> column, InnerQueryFieldsProvider<? extends R, F> val) {
        setWhereWrapper(new WhereAnd().in(column, val));
        return (Q) this;
    }

    /** Sets a {@code column NOT IN (subquery)} condition, the subquery returning a single field. */
    default <E extends Entity, F, R extends RelatedTo> Q whereNotIn(Field<E, F, R> column, InnerQueryFieldsProvider<? extends R, F> val) {
        setWhereWrapper(new WhereAnd().notIn(column, val));
        return (Q) this;
    }

    /** Sets equality conditions for every column of the composite key, matched against {@code val}. */
    default <K extends Composite, K1 extends Composite> Q whereEqual(CompositeColumns<K> key, K1 val) {
        setWhereWrapper(new WhereAnd().equal(key, val));
        return (Q) this;
    }

    /** Sets equality conditions for every column of the composite key, matched against {@code val}. */
    default <K extends Composite, K1 extends Composite> Q whereNotEqual(CompositeColumns<K> key, K1 val) {
        setWhereWrapper(new WhereAnd().equal(key, val));
        return (Q) this;
    }
}
