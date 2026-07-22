package io.daobab.query.base;

import io.daobab.model.Column;
import io.daobab.model.ColumnHaving;
import io.daobab.model.Entity;
import io.daobab.model.RelatedTo;
import io.daobab.statement.condition.Having;
import io.daobab.statement.function.type.ColumnFunction;
import io.daobab.statement.inner.InnerQueryFieldsProvider;
import io.daobab.statement.where.WhereAnd;
import io.daobab.statement.where.WhereOr;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * The {@code HAVING} fragment of a query - the filter applied to grouped results, usually over aggregate
 * {@link ColumnFunction}s. Like the {@code whereXxx} shortcuts, each {@code havingXxx} shortcut <b>replaces</b>
 * the whole having clause with one condition; build a {@link Having} explicitly (or use
 * {@link #havingAnd(Supplier)} / {@link #havingOR(Supplier)}) to combine several. For example:
 * <pre>{@code
 * db.select(tabOrder.colCustomerId(), count(tabOrder.colId()))
 *   .groupBy(tabOrder.colCustomerId())
 *   .havingGreater(count(tabOrder.colId()), 5)   // more than 5 orders
 *   .findMany();
 * }</pre>
 *
 * @param <Q> the concrete query type, returned for chaining
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings({"unchecked", "rawtypes", "UnusedReturnValue", "unused"})
public interface QueryHaving<Q extends Query> {

    /**
     * Sets the having clause (implementation hook).
     */
    void setHavingWrapper(Having havingWrapper);

    /** Sets the having clause to the given one. */
    default Q having(Having wrapper) {
        setHavingWrapper(wrapper);
        return (Q) this;
    }

    /** Sets the having clause to an {@code AND} group built by the supplier. */
    default Q havingAnd(Supplier<WhereAnd> wrapper) {
        setHavingWrapper(new Having(new Having(wrapper.get())));
        return (Q) this;
    }

    /** Sets the having clause to an {@code OR} group built by the supplier. */
    default Q havingOR(Supplier<WhereOr> wrapper) {
        setHavingWrapper(new Having(wrapper.get()));
        return (Q) this;
    }

    /** Sets a {@code function IN (subquery)} having condition. */
    default <F extends Number, R extends RelatedTo> Q having(ColumnFunction<? extends Entity, F, R, F> col, InnerQueryFieldsProvider<? extends R, F> val) {
        setHavingWrapper(new Having().in(col, val));
        return (Q) this;
    }

    /** Sets a {@code function = value} having condition. */
    default <E extends Entity, F extends Number, R extends RelatedTo> Q having(ColumnFunction<E, F, R, F> column, F val) {
        setHavingWrapper(new Having().equal(column, val));
        return (Q) this;
    }

    /** Sets a {@code function = value} having condition, the value taken from the related entity. */
    default <E extends Entity, F extends Number, R extends RelatedTo> Q having(ColumnFunction<E, F, R, F> column, R val) {
        setHavingWrapper(new Having().equal(column, column.getValueOf(val)));
        return (Q) this;
    }

    /** Sets a {@code function = value} having condition only when {@code val} is not {@code null}. */
    default <E extends Entity, F extends Number, R extends RelatedTo> Q havingIfNotNull(ColumnFunction<E, F, R, F> column, F val) {
        if (val != null) setHavingWrapper(new Having().ifNotNull(column, val));
        return (Q) this;
    }

    /** Sets a {@code function IS NULL} having condition. */
    default <F extends Number> Q havingIsNull(ColumnFunction<?, F, ?, F> column) {
        setHavingWrapper(new Having().isNull(column));
        return (Q) this;
    }

    /** Sets a {@code function IS NOT NULL} having condition. */
    default <F extends Number> Q havingIsNotNull(ColumnFunction<?, F, ?, F> column) {
        setHavingWrapper(new Having().notNull(column));
        return (Q) this;
    }

    /** Sets a {@code column = value} having condition. */
    default <E extends Entity, F, R extends RelatedTo> Q havingEqual(Column<E, F, R> column, F val) {
        setHavingWrapper(new Having().equal(column, val));
        return (Q) this;
    }

    /** Sets a {@code column <> value} having condition. */
    default <E extends Entity, F, R extends RelatedTo> Q havingNotEqual(Column<E, F, R> column, F val) {
        setHavingWrapper(new Having().notEqual(column, val));
        return (Q) this;
    }

    /** Sets a {@code column > value} having condition. */
    default <E extends Entity, F, R extends RelatedTo> Q havingGreater(Column<E, F, R> column, F val) {
        setHavingWrapper(new Having().greater(column, val));
        return (Q) this;
    }

    /** Sets a {@code column >= value} having condition. */
    default <E extends Entity, F, R extends RelatedTo> Q havingGreaterOrEqual(Column<E, F, R> column, F val) {
        setHavingWrapper(new Having().greaterOrEqual(column, val));
        return (Q) this;
    }

    /** Sets a {@code column < value} having condition. */
    default <E extends Entity, F, R extends RelatedTo> Q havingLess(Column<E, F, R> column, F val) {
        setHavingWrapper(new Having().less(column, val));
        return (Q) this;
    }

    /** Sets a {@code column <= value} having condition. */
    default <E extends Entity, F, R extends RelatedTo> Q havingLessOrEqual(Column<E, F, R> column, F val) {
        setHavingWrapper(new Having().lessOrEqual(column, val));
        return (Q) this;
    }

    /** Sets a {@code column LIKE value} having condition. */
    default <E extends Entity, F, R extends RelatedTo> Q havingLike(Column<E, F, R> column, F val) {
        setHavingWrapper(new Having().like(column, val));
        return (Q) this;
    }

    /** Sets a {@code column = value} having condition, the value taken from the related entity. */
    default <E extends Entity, F, R extends RelatedTo> Q havingEqual(Column<E, F, R> column, R val) {
        setHavingWrapper(new Having().equal(column, val));
        return (Q) this;
    }

    /** Sets a {@code column <> value} having condition, the value taken from the related entity. */
    default <E extends Entity, F, R extends RelatedTo> Q havingNotEqual(Column<E, F, R> column, R val) {
        setHavingWrapper(new Having().notEqual(column, val));
        return (Q) this;
    }

    /** Sets a {@code column > value} having condition, the value taken from the related entity. */
    default <E extends Entity, F, R extends RelatedTo> Q havingGreater(Column<E, F, R> column, R val) {
        setHavingWrapper(new Having().greater(column, val));
        return (Q) this;
    }

    /** Sets a {@code column >= value} having condition, the value taken from the related entity. */
    default <E extends Entity, F, R extends RelatedTo> Q havingGreaterOrEqual(Column<E, F, R> column, R val) {
        setHavingWrapper(new Having().greaterOrEqual(column, val));
        return (Q) this;
    }

    /** Sets a {@code column < value} having condition, the value taken from the related entity. */
    default <E extends Entity, F, R extends RelatedTo> Q havingLess(Column<E, F, R> column, R val) {
        setHavingWrapper(new Having().less(column, val));
        return (Q) this;
    }

    /** Sets a {@code column <= value} having condition, the value taken from the related entity. */
    default <E extends Entity, F, R extends RelatedTo> Q havingLessOrEqual(Column<E, F, R> column, R val) {
        setHavingWrapper(new Having().lessOrEqual(column, val));
        return (Q) this;
    }

    /** Sets a {@code column IN (values)} having condition. */
    default <E extends Entity, F, R extends RelatedTo> Q havingIn(Column<E, F, R> column, F... val) {
        setHavingWrapper(new Having().in(column, val));
        return (Q) this;
    }

    /** Sets a {@code column NOT IN (values)} having condition. */
    default <E extends Entity, F, R extends RelatedTo> Q havingNotIn(Column<E, F, R> column, F... val) {
        setHavingWrapper(new Having().notIn(column, val));
        return (Q) this;
    }


    /** Sets a {@code column IN (values)} having condition over a collection of field values. */
    default <E extends Entity, F, R extends RelatedTo> Q havingIn(Column<E, F, R> column, Collection<F> val) {
        setHavingWrapper(new Having().inFields(column, val));
        return (Q) this;
    }

    /** Sets a {@code column NOT IN (values)} having condition over a collection of field values. */
    default <E extends Entity, F, R extends RelatedTo> Q havingNotIn(Column<E, F, R> column, Collection<F> val) {
        setHavingWrapper(new Having().notInFields(column, val));
        return (Q) this;
    }

    /** Sets a {@code column IN (...)} having condition over the values of the related entities. */
    default <E extends Entity, F, R extends RelatedTo> Q havingIn(Column<E, F, R> column, R... val) {
        setHavingWrapper(new Having().in(column, val));
        return (Q) this;
    }

    /** Sets a {@code column NOT IN (...)} having condition over the values of the related entities. */
    default <E extends Entity, F, R extends RelatedTo> Q havingNotIn(Column<E, F, R> column, R... val) {
        setHavingWrapper(new Having().notIn(column, val));
        return (Q) this;
    }

    /** Sets a {@code column = column2} having condition comparing two columns. */
    default <F> Q havingEqual(Column<?, F, ?> column, Column<?, F, ?> column2) {
        setHavingWrapper(new Having().equal(column, column2));
        return (Q) this;
    }

    /** Sets a {@code column > column2} having condition comparing two columns. */
    default <F> Q havingGreater(Column<?, F, ?> column, Column<?, F, ?> column2) {
        setHavingWrapper(new Having().greater(column, column2));
        return (Q) this;
    }

    /** Sets a {@code column >= column2} having condition comparing two columns. */
    default <F> Q havingGreaterOrEqual(Column<?, F, ?> column, Column<?, F, ?> column2) {
        setHavingWrapper(new Having().greaterOrEqual(column, column2));
        return (Q) this;
    }

    /** Sets a {@code column < column2} having condition comparing two columns. */
    default <F> Q havingLess(Column<?, F, ?> column, Column<?, F, ?> column2) {
        setHavingWrapper(new Having().less(column, column2));
        return (Q) this;
    }

    /** Sets a {@code column <= column2} having condition comparing two columns. */
    default <F> Q havingLessOrEqual(Column<?, F, ?> column, Column<?, F, ?> column2) {
        setHavingWrapper(new Having().lessOrEqual(column, column2));
        return (Q) this;
    }

    /** Sets a {@code function = function2} having condition comparing two aggregates. */
    default <F extends Number> Q havingEqual(ColumnFunction<?, F, ?, F> column, ColumnFunction<?, F, ?, F> column2) {
        setHavingWrapper(new Having(new WhereAnd().equal(column, column2)));
        return (Q) this;
    }

    /** Sets a strictly-between having condition on an aggregate (greater than {@code valueFrom}, less than {@code valueTo}). */
    default <F extends Number> Q havingBetween(ColumnFunction<?, F, ?, F> column, F valueFrom, F valueTo) {
        setHavingWrapper(new Having(new WhereAnd().between(column, valueFrom, valueTo)));
        return (Q) this;
    }

    /** Sets a strictly-between having condition on an aggregate, the bounds taken from related entities. */
    default <F extends Number, R extends RelatedTo> Q havingBetween(ColumnFunction<?, F, R, F> column, R valueFrom, R valueTo) {
        setHavingWrapper(new Having(new WhereAnd().between(column, valueFrom, valueTo)));
        return (Q) this;
    }

    /** Sets a {@code column = value} having condition, the column addressed by its identifier/alias. */
    default Q havingEqual(String column, Object val) {
        havingEqual(new ColumnHaving(column, true), val);
        return (Q) this;
    }

    /** Sets a {@code column > value} having condition, the column addressed by its identifier/alias. */
    default Q havingGreater(String column, Object val) {
        havingGreater(new ColumnHaving(column, true), val);
        return (Q) this;
    }

    /** Sets a {@code column >= value} having condition, the column addressed by its identifier/alias. */
    default Q havingGreaterOrEqual(String column, Object val) {
        havingGreaterOrEqual(new ColumnHaving(column, true), val);
        return (Q) this;
    }

    /** Sets a {@code column < value} having condition, the column addressed by its identifier/alias. */
    default Q havingLess(String column, Object val) {
        havingLess(new ColumnHaving(column, true), val);
        return (Q) this;
    }

    /** Sets a {@code column <= value} having condition, the column addressed by its identifier/alias. */
    default Q havingLessOrEqual(String column, Object val) {
        havingLessOrEqual(new ColumnHaving(column, true), val);
        return (Q) this;
    }

    /** Sets a {@code column <> value} having condition, the column addressed by its identifier/alias. */
    default Q havingNotEqual(String column, Object val) {
        havingNotEqual(new ColumnHaving(column, true), val);
        return (Q) this;
    }

    /** Sets a {@code column = (subquery)} having condition, the subquery returning a single field. */
    default Q havingEqual(Column<?, ?, ?> column, InnerQueryFieldsProvider val) {
        setHavingWrapper(new Having(new WhereAnd().equal(column, val)));
        return (Q) this;
    }

    /** Sets a {@code column > (subquery)} having condition, the subquery returning a single field. */
    default Q havingGreater(Column<?, ?, ?> column, InnerQueryFieldsProvider val) {
        setHavingWrapper(new Having(new WhereAnd().greater(column, val)));
        return (Q) this;
    }

    /** Sets a {@code column >= (subquery)} having condition, the subquery returning a single field. */
    default Q havingGreaterOrEqual(Column<?, ?, ?> column, InnerQueryFieldsProvider val) {
        setHavingWrapper(new Having(new WhereAnd().greaterOrEqual(column, val)));
        return (Q) this;
    }

    /** Sets a {@code column < (subquery)} having condition, the subquery returning a single field. */
    default Q havingLess(Column<?, ?, ?> column, InnerQueryFieldsProvider val) {
        setHavingWrapper(new Having(new WhereAnd().less(column, val)));
        return (Q) this;
    }

    /** Sets a {@code column <= (subquery)} having condition, the subquery returning a single field. */
    default Q havingLessOrEqual(Column<?, ?, ?> column, InnerQueryFieldsProvider val) {
        setHavingWrapper(new Having(new WhereAnd().lessOrEqual(column, val)));
        return (Q) this;
    }

    /** Sets a {@code column <> (subquery)} having condition, the subquery returning a single field. */
    default Q havingNotEqual(Column<?, ?, ?> column, InnerQueryFieldsProvider val) {
        setHavingWrapper(new Having(new WhereAnd().notEqual(column, val)));
        return (Q) this;
    }

}
