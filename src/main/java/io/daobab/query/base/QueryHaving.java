package io.daobab.query.base;

import io.daobab.model.Column;
import io.daobab.model.ColumnHaving;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;
import io.daobab.statement.condition.Having;
import io.daobab.statement.function.base.ColumnFunction;
import io.daobab.statement.inner.InnerQueryField;
import io.daobab.statement.where.WhereAnd;
import io.daobab.statement.where.WhereOr;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
@SuppressWarnings({"unchecked","rawtypes","UnusedReturnValue","unused"})
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


    default <F extends Number, R extends EntityRelation> Q having(ColumnFunction<? extends Entity, F, R, F> col, InnerQueryField<? extends R, F> val) {
//        setHavingWrapper(new Having().in(col, val));
        return (Q) this;
    }


    /**
     * Condition: column value must be EQUAL to value
     *
     * @param column - representation of table column or class field  - table column
     * @param val    - value - value
     * @param <E>    Entity type (E extends io.daobab.model.Entity)<br>
     * @param <F>    value type
     * @param <R>    ColumnRelatedFunction(AVG,SUM,COUNT,MIN,MAX...), might also return F value
     * @return WhereAND
     */
    default <E extends Entity, F extends Number, R extends EntityRelation> Q having(ColumnFunction<E, F, R, F> column, F val) {
        setHavingWrapper(new Having().equal(column, val));
        return (Q) this;
    }

    /**
     * Condition: column value must be EQUAL to value taken from related object.
     *
     * @param column - representation of table column or class field
     * @param val    - value
     * @param <E>    Entity type (E extends io.daobab.model.Entity)<br>
     * @param <F>    value type
     * @param <R>    ColumnRelatedFunction(AVG,SUM,COUNT,MIN,MAX...), might also return F value
     * @return WhereAND
     */
    default <E extends Entity, F extends Number, R extends EntityRelation> Q having(ColumnFunction<E, F, R, F> column, R val) {
        setHavingWrapper(new Having().equal(column, column.getValueOf(val)));
        return (Q) this;
    }


    /**
     * Condition: column value can not be NULL. Must be EQUAL to value
     * WARNING: if val is null, the condition will be rejected.
     *
     * @param column - representation of table column or class field
     * @param val    - value
     * @param <E>    Entity type (E extends io.daobab.model.Entity)<br>
     * @param <F>    value type
     * @param <R>    ColumnRelatedFunction(AVG,SUM,COUNT,MIN,MAX...), might also return F value
     * @return WhereAND
     */
    default <E extends Entity, F extends Number, R extends EntityRelation> Q havingIfNotNull(ColumnFunction<E, F, R, F> column, F val) {
        if (val != null) setHavingWrapper(new Having().ifNotNull(column, val));
        return (Q) this;
    }


    /**
     * Condition: column feature must be specified by operator. Allowed operators: (IS_NULL, NOT NULL)
     *
     * @param column   - representation of table column or class field
     * @param <F>      value type
     * @return WhereAND
     */
    default <F extends Number> Q havingIsNull(ColumnFunction<?, F, ?, F> column) {
        setHavingWrapper(new Having().isNull(column));
        return (Q) this;
    }

    default <F extends Number> Q havingIsNotNull(ColumnFunction<?, F, ?, F> column) {
        setHavingWrapper(new Having().notNull(column));
        return (Q) this;
    }


    /**
     * Condition: consider only those records, matching the value with specified relation
     *
     * @param column   - representation of table column or class field
//     * @param operator - logical relation
     * @param val      - value
     * @param <E>      Entity type (E extends io.daobab.model.Entity)<br>
     * @param <F>      value type
     * @param <R>      ColumnRelatedFunction(AVG,SUM,COUNT,MIN,MAX...), might also return F value
     * @return WhereAND
     */

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

    /**
     * Condition: consider only those records, matching the value taken from related object, with specified relation
     *
     * @param column   - representation of table column or class field
//     * @param operator - logical relation
     * @param val      - value
     * @param <E>      Entity type (E extends io.daobab.model.Entity)<br>
     * @param <F>      value type
     * @param <R>      ColumnRelatedFunction(AVG,SUM,COUNT,MIN,MAX...), might also return F value
     * @return WhereAND
     */
//    default <E extends Entity, F extends Number, R extends EntityRelation> Q having(ColumnFunction<E, F, R, F> column, Operator operator, R val) {
//        setHavingWrapper(new Having(new WhereAND().and(column, operator, val)));
//        return (Q) this;
//    }


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


    /**
     * @param column   - representation of table column or class field
     * @param val      - value
     * @param <E>      Entity type (E extends io.daobab.model.Entity)<br>
     * @param <F>      value type
     * @param <R>      ColumnRelatedFunction(AVG,SUM,COUNT,MIN,MAX...), might also return F value
     * @return WhereAND
     */
//    default <E extends Entity, F extends Number, R extends EntityRelation> Q having(ColumnFunction<E, F, R, F> column, Operator operator, F... val) {
//        setHavingWrapper(new Having(new WhereAND().and(column, operator, val)));
//        return (Q) this;
//    }

    default <E extends Entity, F, R extends EntityRelation> Q havingIn(Column<E, F, R> column, F... val) {
        setHavingWrapper(new Having().in(column, val));
        return (Q) this;
    }
    default <E extends Entity, F, R extends EntityRelation> Q havingNotIn(Column<E, F, R> column, F... val) {
        setHavingWrapper(new Having().notIn(column, val));
        return (Q) this;
    }

    /**
     * @param column   - representation of table column or class field
//     * @param operator - logical relation
     * @param val      - value
     * @param <E>      Entity type (E extends io.daobab.model.Entity)<br>
     * @param <F>      value type
     * @param <R>      ColumnRelatedFunction(AVG,SUM,COUNT,MIN,MAX...), might also return F value
     * @return WhereAND
     */
//    default <E extends Entity, F extends Number, R extends EntityRelation> Q having(ColumnFunction<E, F, R, F> column, Operator operator, R... val) {
//        setHavingWrapper(new Having(new WhereAND().and(column, operator, val)));
//        return (Q) this;
//    }


    default <E extends Entity, F, R extends EntityRelation> Q havingIn(Column<E, F, R> column, Collection<F> val) {
        setHavingWrapper(new Having().in(column, val));
        return (Q) this;
    }
    default <E extends Entity, F, R extends EntityRelation> Q havingNotIn(Column<E, F, R> column, Collection<F> val) {
        setHavingWrapper(new Having().notIn(column, val));
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

//    /**
//     * @param column   - representation of table column or class field
//     * @param operator - logical relation
//     * @param val      - value
//     * @param <F>      value type
//     * @param <R>      ColumnRelatedFunction(AVG,SUM,COUNT,MIN,MAX...), might also return F value
//     * @return WhereAND
//     */
//    default <F extends Number, R extends EntityRelation> Q having(ColumnFunction<?, F, R, F> column, Operator operator, Entities<? extends R> val) {
//        setHavingWrapper(new Having(new WhereAND().and(column, operator, val)));
//        return (Q) this;
//    }


    /**
     * Condition: consider only those records, having EQUAL values in both columns
     *
     * @param column  - representation of table column or class field
     * @param column2 - representation of table column or class field
     * @param <F>     value type
     * @return WhereAND
     */
//    default <F extends Number> Q having(ColumnFunction<?, F, ?, F> column, ColumnFunction<?, F, ?, F> column2) {
//        setHavingWrapper(new Having(new WhereAND().and(column, Operator.EQ, column2)));
//        return (Q) this;
//    }

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

    /**
     * Condition: consider only those records, having specified relation between values in both columns
     *
     * @param column  - representation of table column or class field
     * @param column2 - representation of table column or class field
     * @param <F>     value type
     * @return WhereAND
     */
    default <F extends Number> Q havingEqual(ColumnFunction<?, F, ?, F> column,  ColumnFunction<?, F, ?, F> column2) {
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

    default <F extends Number, R extends EntityRelation> Q havingEqual(String column, Object val) {
        havingEqual(new ColumnHaving(column),   val);
        return (Q)this;
    }

    default <F extends Number, R extends EntityRelation> Q havingGreater(String column, Object val) {
        havingEqual(new ColumnHaving(column),val);
        return (Q)this;
    }
    default <F extends Number, R extends EntityRelation> Q havingGreaterOrEqual(String column, Object val) {
        havingEqual(new ColumnHaving(column),val);
        return (Q)this;
    }
    default <F extends Number, R extends EntityRelation> Q havingLess(String column, Object val) {
        havingEqual(new ColumnHaving(column),val);
        return (Q)this;
    }
    default <F extends Number, R extends EntityRelation> Q havingLessOrEqual(String column, Object val) {
        havingEqual(new ColumnHaving(column),val);
        return (Q)this;
    }
    default <F extends Number, R extends EntityRelation> Q havingNotEqual(String column, Object val) {
        havingEqual(new ColumnHaving(column), val);
        return (Q)this;
    }


}
