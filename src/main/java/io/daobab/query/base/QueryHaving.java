package io.daobab.query.base;

import io.daobab.model.Column;
import io.daobab.model.ColumnHaving;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;
import io.daobab.statement.condition.Having;
import io.daobab.statement.function.type.ColumnFunction;
import io.daobab.statement.inner.InnerQueryFieldsProvider;
import io.daobab.statement.where.WhereAnd;
import io.daobab.statement.where.WhereOr;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
@SuppressWarnings({"unchecked", "rawtypes", "UnusedReturnValue", "unused"})
public interface QueryHaving<Q extends Query> {

    void setHavingWrapper(Having havingWrapper);

    default Q having(Having wrapper) {
        setHavingWrapper(wrapper);
        return (Q) this;
    }

    default Q havingAnd(Supplier<WhereAnd> wrapper) {
        setHavingWrapper(new Having(new Having(wrapper.get())));
        return (Q) this;
    }

    default Q havingOR(Supplier<WhereOr> wrapper) {
        setHavingWrapper(new Having(wrapper.get()));
        return (Q) this;
    }

    default <F extends Number, R extends EntityRelation> Q having(ColumnFunction<? extends Entity, F, R, F> col, InnerQueryFieldsProvider<? extends R, F> val) {
        setHavingWrapper(new Having().in(col, val));
        return (Q) this;
    }

    default <E extends Entity, F extends Number, R extends EntityRelation> Q having(ColumnFunction<E, F, R, F> column, F val) {
        setHavingWrapper(new Having().equal(column, val));
        return (Q) this;
    }

    default <E extends Entity, F extends Number, R extends EntityRelation> Q having(ColumnFunction<E, F, R, F> column, R val) {
        setHavingWrapper(new Having().equal(column, column.getValueOf(val)));
        return (Q) this;
    }

    default <E extends Entity, F extends Number, R extends EntityRelation> Q havingIfNotNull(ColumnFunction<E, F, R, F> column, F val) {
        if (val != null) setHavingWrapper(new Having().ifNotNull(column, val));
        return (Q) this;
    }

    default <F extends Number> Q havingIsNull(ColumnFunction<?, F, ?, F> column) {
        setHavingWrapper(new Having().isNull(column));
        return (Q) this;
    }

    default <F extends Number> Q havingIsNotNull(ColumnFunction<?, F, ?, F> column) {
        setHavingWrapper(new Having().notNull(column));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q havingEqual(Column<E, F, R> column, F val) {
        setHavingWrapper(new Having().equal(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q havingNotEqual(Column<E, F, R> column, F val) {
        setHavingWrapper(new Having().notEqual(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q havingGreater(Column<E, F, R> column, F val) {
        setHavingWrapper(new Having().greater(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q havingGreaterOrEqual(Column<E, F, R> column, F val) {
        setHavingWrapper(new Having().greaterOrEqual(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q havingLess(Column<E, F, R> column, F val) {
        setHavingWrapper(new Having().less(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q havingLessOrEqual(Column<E, F, R> column, F val) {
        setHavingWrapper(new Having().lessOrEqual(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q havingLike(Column<E, F, R> column, F val) {
        setHavingWrapper(new Having().like(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q havingEqual(Column<E, F, R> column, R val) {
        setHavingWrapper(new Having().equal(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q havingNotEqual(Column<E, F, R> column, R val) {
        setHavingWrapper(new Having().notEqual(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q havingGreater(Column<E, F, R> column, R val) {
        setHavingWrapper(new Having().greater(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q havingGreaterOrEqual(Column<E, F, R> column, R val) {
        setHavingWrapper(new Having().greaterOrEqual(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q havingLess(Column<E, F, R> column, R val) {
        setHavingWrapper(new Having().less(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q havingLessOrEqual(Column<E, F, R> column, R val) {
        setHavingWrapper(new Having().lessOrEqual(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q havingIn(Column<E, F, R> column, F... val) {
        setHavingWrapper(new Having().in(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q havingNotIn(Column<E, F, R> column, F... val) {
        setHavingWrapper(new Having().notIn(column, val));
        return (Q) this;
    }


    default <E extends Entity, F, R extends EntityRelation> Q havingIn(Column<E, F, R> column, Collection<F> val) {
        setHavingWrapper(new Having().inFields(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q havingNotIn(Column<E, F, R> column, Collection<F> val) {
        setHavingWrapper(new Having().notInFields(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q havingIn(Column<E, F, R> column, R... val) {
        setHavingWrapper(new Having().in(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q havingNotIn(Column<E, F, R> column, R... val) {
        setHavingWrapper(new Having().notIn(column, val));
        return (Q) this;
    }

    default <F> Q havingEqual(Column<?, F, ?> column, Column<?, F, ?> column2) {
        setHavingWrapper(new Having().equal(column, column2));
        return (Q) this;
    }

    default <F> Q havingGreater(Column<?, F, ?> column, Column<?, F, ?> column2) {
        setHavingWrapper(new Having().greater(column, column2));
        return (Q) this;
    }

    default <F> Q havingGreaterOrEqual(Column<?, F, ?> column, Column<?, F, ?> column2) {
        setHavingWrapper(new Having().greaterOrEqual(column, column2));
        return (Q) this;
    }

    default <F> Q havingLess(Column<?, F, ?> column, Column<?, F, ?> column2) {
        setHavingWrapper(new Having().less(column, column2));
        return (Q) this;
    }

    default <F> Q havingLessOrEqual(Column<?, F, ?> column, Column<?, F, ?> column2) {
        setHavingWrapper(new Having().lessOrEqual(column, column2));
        return (Q) this;
    }

    default <F extends Number> Q havingEqual(ColumnFunction<?, F, ?, F> column, ColumnFunction<?, F, ?, F> column2) {
        setHavingWrapper(new Having(new WhereAnd().equal(column, column2)));
        return (Q) this;
    }

    default <F extends Number> Q havingBetween(ColumnFunction<?, F, ?, F> column, F valueFrom, F valueTo) {
        setHavingWrapper(new Having(new WhereAnd().between(column, valueFrom, valueTo)));
        return (Q) this;
    }

    default <F extends Number, R extends EntityRelation> Q havingBetween(ColumnFunction<?, F, R, F> column, R valueFrom, R valueTo) {
        setHavingWrapper(new Having(new WhereAnd().between(column, valueFrom, valueTo)));
        return (Q) this;
    }

    default Q havingEqual(String column, Object val) {
        havingEqual(new ColumnHaving(column), val);
        return (Q) this;
    }

    default Q havingGreater(String column, Object val) {
        havingGreater(new ColumnHaving(column), val);
        return (Q) this;
    }

    default Q havingGreaterOrEqual(String column, Object val) {
        havingGreaterOrEqual(new ColumnHaving(column), val);
        return (Q) this;
    }

    default Q havingLess(String column, Object val) {
        havingLess(new ColumnHaving(column), val);
        return (Q) this;
    }

    default Q havingLessOrEqual(String column, Object val) {
        havingLessOrEqual(new ColumnHaving(column), val);
        return (Q) this;
    }

    default Q havingNotEqual(String column, Object val) {
        havingNotEqual(new ColumnHaving(column), val);
        return (Q) this;
    }

    default Q havingEqual(Column<?, ?, ?> column, InnerQueryFieldsProvider val) {
        setHavingWrapper(new Having(new WhereAnd().equal(column, val)));
        return (Q) this;
    }

    default Q havingGreater(Column<?, ?, ?> column, InnerQueryFieldsProvider val) {
        setHavingWrapper(new Having(new WhereAnd().greater(column, val)));
        return (Q) this;
    }

    default Q havingGreaterOrEqual(Column<?, ?, ?> column, InnerQueryFieldsProvider val) {
        setHavingWrapper(new Having(new WhereAnd().greaterOrEqual(column, val)));
        return (Q) this;
    }

    default Q havingLess(Column<?, ?, ?> column, InnerQueryFieldsProvider val) {
        setHavingWrapper(new Having(new WhereAnd().less(column, val)));
        return (Q) this;
    }

    default Q havingLessOrEqual(Column<?, ?, ?> column, InnerQueryFieldsProvider val) {
        setHavingWrapper(new Having(new WhereAnd().lessOrEqual(column, val)));
        return (Q) this;
    }

    default Q havingNotEqual(Column<?, ?, ?> column, InnerQueryFieldsProvider val) {
        setHavingWrapper(new Having(new WhereAnd().notEqual(column, val)));
        return (Q) this;
    }

}
