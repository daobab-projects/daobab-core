package io.daobab.statement.where.base;

import io.daobab.error.*;
import io.daobab.generator.DictRemoteKey;
import io.daobab.model.*;
import io.daobab.statement.condition.Operator;
import io.daobab.statement.inner.InnerQueryEntity;
import io.daobab.statement.inner.InnerQueryFieldsProvider;
import io.daobab.statement.where.WhereAnd;
import io.daobab.statement.where.WhereNot;
import io.daobab.statement.where.WhereOr;
import io.daobab.target.Target;
import io.daobab.target.buffer.single.Entities;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static io.daobab.statement.condition.Operator.*;

/**
 * The building block of a SQL {@code WHERE} clause: a group of conditions combined by one boolean relation
 * (AND/OR/NOT - see {@link #getRelationBetweenExpressions()}). Conditions are added with the fluent
 * {@code equal}/{@code greater}/{@code less}/{@code like}/{@code in}/{@code between}/{@code isNull}/... methods,
 * each returning this group ({@code W}) for chaining; nested {@link WhereAnd}/{@link WhereOr}/{@link WhereNot}
 * groups build an arbitrary boolean tree.
 * <p>
 * Most operators come in several overloads that differ by the shape of the right-hand side: a plain field value
 * ({@code F}), a related entity ({@code R}), another {@link Column}/{@link Field}, an inner (sub)query, or a
 * collection. The conditions are stored positionally in the underlying map (see {@link WhereBase}); the private
 * {@code temp(...)} helpers funnel every overload into that storage, validating the mandatory column, operator
 * and value.
 *
 * @param <W> the concrete clause type, returned by the fluent methods
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings({"unchecked", "rawtypes", "unused", "UnusedReturnValue"})
public abstract class Where<W extends Where> extends WhereBase {

    /**
     * Whether no condition has been added to this group yet.
     */
    public boolean isEmpty() {
        return this.getCounter() == 1;
    }

    /** Applies {@code condition} only when {@code enabled} is {@code true}, otherwise leaves this group unchanged. */
    public final W ifTrue(boolean enabled, UnaryOperator<W> condition) {
        if (enabled) {
            return condition.apply((W) this);
        }
        return (W) this;
    }

    /** Applies {@code condition} only when {@code enabled} is {@code false}. */
    public final W ifFalse(boolean enabled, UnaryOperator<W> condition) {
        if (!enabled) {
            return condition.apply((W) this);
        }
        return (W) this;
    }

    /** Applies {@code ifTrue} when {@code enabled}, otherwise {@code ifFalse}. */
    public final W ifElse(boolean enabled, UnaryOperator<W> ifTrue, UnaryOperator<W> ifFalse) {
        if (enabled) {
            return ifTrue.apply((W) this);
        } else {
            return ifFalse.apply((W) this);
        }
    }

    /** Applies {@code condition} only when {@code predicate} accepts {@code val}. */
    public final <X> W ifTrue(Predicate<X> predicate, X val, UnaryOperator<W> condition) {
        if (predicate.test(val)) {
            return condition.apply((W) this);
        }
        return (W) this;
    }

    /** Applies {@code condition} only when {@code predicate} rejects {@code val}. */
    public final <X> W ifFalse(Predicate<X> predicate, X val, UnaryOperator<W> condition) {
        if (!predicate.test(val)) {
            return condition.apply((W) this);
        }
        return (W) this;
    }

    /** Applies {@code ifTrue} when {@code predicate} accepts {@code val}, otherwise {@code ifFalse}. */
    public final <X> W ifElse(Predicate<X> predicate, X val, UnaryOperator<W> ifTrue, UnaryOperator<W> ifFalse) {
        if (predicate.test(val)) {
            return ifTrue.apply((W) this);
        } else {
            return ifFalse.apply((W) this);
        }
    }


    /** Adds a {@code column = value} condition only when {@code val} is not {@code null}. */
    public final <E extends Entity, F, R extends RelatedTo> W ifNotNull(Field<E, F, R> column, F val) {
        if (val != null) temp(column, val);
        return (W) this;
    }


    /** Adds a {@code column operator value} condition only when {@code val} is not {@code null}. */
    public final <E extends Entity, F, R extends RelatedTo> W ifNotNull(Field<E, F, R> column, Operator operator, F val) {
        if (val != null) temp(column, operator, val);
        return (W) this;
    }


    /** Adds a {@code column IS NULL} condition. */
    public final <F> W isNull(Field<?, F, ?> column) {
        temp(column, IS_NULL);
        return (W) this;
    }

    /** Adds a {@code column IS NOT NULL} condition. */
    public final <F> W notNull(Field<?, F, ?> column) {
        temp(column, NOT_NULL);
        return (W) this;
    }

    /** Adds a {@code column = value} condition. */
    @SuppressWarnings("java:S1221")
    public final <E extends Entity, F, R extends RelatedTo> W equal(Field<E, F, R> column, F val) {
        temp(column, EQ, val);
        return (W) this;
    }

    /** Adds a {@code column > value} condition. */
    public final <E extends Entity, F, R extends RelatedTo> W greater(Field<E, F, R> column, F val) {
        temp(column, GT, val);
        return (W) this;
    }

    /** Adds a {@code column >= value} condition. */
    public final <E extends Entity, F, R extends RelatedTo> W greaterOrEqual(Field<E, F, R> column, F val) {
        temp(column, GTEQ, val);
        return (W) this;
    }

    /** Adds a {@code column < value} condition. */
    public final <E extends Entity, F, R extends RelatedTo> W less(Field<E, F, R> column, F val) {
        temp(column, LT, val);
        return (W) this;
    }

    /** Adds a {@code column <= value} condition. */
    public final <E extends Entity, F, R extends RelatedTo> W lessOrEqual(Field<E, F, R> column, F val) {
        temp(column, LTEQ, val);
        return (W) this;
    }

    /** Adds a {@code column <> value} condition. */
    public final <E extends Entity, F, R extends RelatedTo> W notEqual(Field<E, F, R> column, F val) {
        temp(column, NOT_EQ, val);
        return (W) this;
    }

    /** Adds a {@code column = value} condition, the value taken from the related entity {@code val}. */
    @SuppressWarnings("java:S1221")
    public final <E extends Entity, F, R extends RelatedTo> W equal(Field<E, F, R> column, R val) {
        temp(column, EQ, val);
        return (W) this;
    }

    /** Adds a {@code column > value} condition, the value taken from the related entity {@code val}. */
    public final <E extends Entity, F, R extends RelatedTo> W greater(Field<E, F, R> column, R val) {
        temp(column, GT, val);
        return (W) this;
    }

    /** Adds a {@code column >= value} condition, the value taken from the related entity {@code val}. */
    public final <E extends Entity, F, R extends RelatedTo> W greaterOrEqual(Field<E, F, R> column, R val) {
        temp(column, GTEQ, val);
        return (W) this;
    }

    /** Adds a {@code column < value} condition, the value taken from the related entity {@code val}. */
    public final <E extends Entity, F, R extends RelatedTo> W less(Field<E, F, R> column, R val) {
        temp(column, LT, val);
        return (W) this;
    }

    /** Adds a {@code column <= value} condition, the value taken from the related entity {@code val}. */
    public final <E extends Entity, F, R extends RelatedTo> W lessOrEqual(Field<E, F, R> column, R val) {
        temp(column, LTEQ, val);
        return (W) this;
    }

    /** Adds a {@code column <> value} condition, the value taken from the related entity {@code val}. */
    public final <E extends Entity, F, R extends RelatedTo> W notEqual(Field<E, F, R> column, R val) {
        temp(column, NOT_EQ, val);
        return (W) this;
    }

    /** Adds a {@code column = column2} condition, comparing the column to another field. */
    @SuppressWarnings("java:S1221")
    public final <E extends Entity, F, R extends RelatedTo> W equal(Field<E, F, R> column, Field<?, F, ?> val) {
        temp(column, EQ, val);
        return (W) this;
    }

    /** Adds a {@code column > column2} condition, comparing the column to another field. */
    public final <E extends Entity, F, R extends RelatedTo> W greater(Field<E, F, R> column, Field<?, F, ?> val) {
        temp(column, GT, val);
        return (W) this;
    }

    /** Adds a {@code column >= column2} condition, comparing the column to another field. */
    public final <E extends Entity, F, R extends RelatedTo> W greaterOrEqual(Field<E, F, R> column, Field<?, F, ?> val) {
        temp(column, GTEQ, val);
        return (W) this;
    }

    /** Adds a {@code column < column2} condition, comparing the column to another field. */
    public final <E extends Entity, F, R extends RelatedTo> W less(Field<E, F, R> column, Field<?, F, ?> val) {
        temp(column, LT, val);
        return (W) this;
    }

    /** Adds a {@code column <= column2} condition, comparing the column to another field. */
    public final <E extends Entity, F, R extends RelatedTo> W lessOrEqual(Field<E, F, R> column, Field<?, F, ?> val) {
        temp(column, LTEQ, val);
        return (W) this;
    }

    /** Adds a {@code column <> column2} condition, comparing the column to another field. */
    public final <E extends Entity, F, R extends RelatedTo> W notEqual(Field<E, F, R> column, Field<?, F, ?> val) {
        temp(column, NOT_EQ, val);
        return (W) this;
    }

    /** Adds a {@code column LIKE value} condition. */
    public <E extends Entity, F, R extends RelatedTo> W like(Field<E, F, R> column, F val) {
        temp(column, LIKE, val);
        return (W) this;
    }

    /** Adds a {@code column NOT LIKE value} condition. */
    public final <E extends Entity, F, R extends RelatedTo> W notLike(Field<E, F, R> column, F val) {
        temp(column, NOT_LIKE, val);
        return (W) this;
    }


    /** Adds a {@code column IN (values)} condition. */
    public final <E extends Entity, F, R extends RelatedTo> W in(Field<E, F, R> column, F... val) {
        temp(column, IN, val);
        return (W) this;
    }

    /** Adds a {@code column NOT IN (values)} condition. */
    public final <E extends Entity, F, R extends RelatedTo> W notIn(Field<E, F, R> column, F... val) {
        temp(column, NOT_IN, val);
        return (W) this;
    }

    /** Adds a {@code column IN (...)} condition over the values taken from the related entities {@code val}. */
    public final <E extends Entity, F, R extends RelatedTo> W in(Field<E, F, R> column, R... val) {
        temp(column, IN, val);
        return (W) this;
    }

    /** Adds a {@code column NOT IN (...)} condition over the values taken from the related entities {@code val}. */
    public final <E extends Entity, F, R extends RelatedTo> W notIn(Field<E, F, R> column, R... val) {
        temp(column, NOT_IN, val);
        return (W) this;
    }

    /** Adds a {@code column IN (values)} condition over a collection of field values. */
    public final <E extends Entity, F, R extends RelatedTo> W inFields(Field<E, F, R> column, Collection<? super F> val) {
        tempColField(column, IN, val);
        return (W) this;
    }

    /** Adds a {@code column NOT IN (values)} condition over a collection of field values. */
    public final <E extends Entity, F, R extends RelatedTo> W notInFields(Field<E, F, R> column, Collection<? super F> val) {
        tempColField(column, NOT_IN, val);
        return (W) this;
    }

    /** Adds a {@code column IN (...)} condition over a set of entities. */
    public final <E extends Entity, F, R extends RelatedTo> W in(Field<E, F, R> column, Entities<? extends R> val) {
        temp(column, IN, val);
        return (W) this;
    }

    /** Adds a {@code column NOT IN (...)} condition over a set of entities. */
    public final <E extends Entity, F, R extends RelatedTo> W notIn(Field<E, F, R> column, Entities<? extends R> val) {
        temp(column, NOT_IN, val);
        return (W) this;
    }


    /** Adds a condition matching values strictly between the bounds (greater than {@code valueFrom} and less than {@code valueTo}). */
    public final <F> W between(Field<?, F, ?> column, F valueFrom, F valueTo) {
        tempBetween(column, valueFrom, valueTo);
        return (W) this;
    }


    /** Adds a strictly-between condition over the values taken from the related entities {@code valueFrom}/{@code valueTo}. */
    public final <F, R extends RelatedTo> W between(Field<?, F, R> column, R valueFrom, R valueTo) {
        tempBetween(column, valueFrom, valueTo);
        return (W) this;
    }

    /** Adds a {@code column = (subquery)} condition, the entity subquery {@code val} projected to the column. */
    @SuppressWarnings("java:S1221")
    public final <E extends Entity, F, R extends RelatedTo> W equal(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        temp(column, EQ, val);
        return (W) this;
    }

    /** Adds a {@code column > (subquery)} condition, the entity subquery {@code val} projected to the column. */
    public final <E extends Entity, F, R extends RelatedTo> W greater(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        temp(column, GT, val);
        return (W) this;
    }

    /** Adds a {@code column >= (subquery)} condition, the entity subquery {@code val} projected to the column. */
    public final <E extends Entity, F, R extends RelatedTo> W greaterOrEqual(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        temp(column, GTEQ, val);
        return (W) this;
    }

    /** Adds a {@code column < (subquery)} condition, the entity subquery {@code val} projected to the column. */
    public final <E extends Entity, F, R extends RelatedTo> W less(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        temp(column, LT, val);
        return (W) this;
    }

    /** Adds a {@code column <= (subquery)} condition, the entity subquery {@code val} projected to the column. */
    public final <E extends Entity, F, R extends RelatedTo> W lessOrEqual(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        temp(column, LTEQ, val);
        return (W) this;
    }

    /** Adds a {@code column <> (subquery)} condition, the entity subquery {@code val} projected to the column. */
    public final <E extends Entity, F, R extends RelatedTo> W notEqual(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        temp(column, NOT_EQ, val);
        return (W) this;
    }

    /** Adds a {@code column LIKE (subquery)} condition, the entity subquery {@code val} projected to the column. */
    public final <E extends Entity, F, R extends RelatedTo> W like(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        temp(column, LIKE, val);
        return (W) this;
    }

    /** Adds a {@code column NOT LIKE (subquery)} condition, the entity subquery {@code val} projected to the column. */
    public final <E extends Entity, F, R extends RelatedTo> W notLike(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        temp(column, NOT_LIKE, val);
        return (W) this;
    }


    /** Adds a {@code column IN (subquery)} condition, the entity subquery {@code val} projected to the column. */
    public final <E extends Entity, F, R extends RelatedTo> W in(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        temp(column, IN, val);
        return (W) this;
    }

    /** Adds a {@code column NOT IN (subquery)} condition, the entity subquery {@code val} projected to the column. */
    public final <E extends Entity, F, R extends RelatedTo> W notIn(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        temp(column, NOT_IN, val);
        return (W) this;
    }


    /** Adds a {@code column = (subquery)} condition, the subquery {@code val} returning a single field. */
    @SuppressWarnings("java:S1221")
    public final <E extends Entity, F, R extends RelatedTo> W equal(Field<E, F, R> column, InnerQueryFieldsProvider<? extends R, F> val) {
        temp(column, EQ, val);
        return (W) this;
    }

    /** Adds a {@code column > (subquery)} condition, the subquery {@code val} returning a single field. */
    public final <E extends Entity, F, R extends RelatedTo> W greater(Field<E, F, R> column, InnerQueryFieldsProvider<? extends R, F> val) {
        temp(column, GT, val);
        return (W) this;
    }

    /** Adds a {@code column >= (subquery)} condition, the subquery {@code val} returning a single field. */
    public final <E extends Entity, F, R extends RelatedTo> W greaterOrEqual(Field<E, F, R> column, InnerQueryFieldsProvider<? extends R, F> val) {
        temp(column, GTEQ, val);
        return (W) this;
    }

    /** Adds a {@code column < (subquery)} condition, the subquery {@code val} returning a single field. */
    public final <E extends Entity, F, R extends RelatedTo> W less(Field<E, F, R> column, InnerQueryFieldsProvider<? extends R, F> val) {
        temp(column, LT, val);
        return (W) this;
    }

    /** Adds a {@code column <= (subquery)} condition, the subquery {@code val} returning a single field. */
    public final <E extends Entity, F, R extends RelatedTo> W lessOrEqual(Field<E, F, R> column, InnerQueryFieldsProvider<? extends R, F> val) {
        temp(column, LTEQ, val);
        return (W) this;
    }

    /** Adds a {@code column <> (subquery)} condition, the subquery {@code val} returning a single field. */
    public final <E extends Entity, F, R extends RelatedTo> W notEqual(Field<E, F, R> column, InnerQueryFieldsProvider<? extends R, F> val) {
        temp(column, NOT_EQ, val);
        return (W) this;
    }

    /** Adds a {@code column LIKE (subquery)} condition, the subquery {@code val} returning a single field. */
    public final <E extends Entity, F, R extends RelatedTo> W like(Field<E, F, R> column, InnerQueryFieldsProvider<? extends R, F> val) {
        temp(column, LIKE, val);
        return (W) this;
    }

    /** Adds a {@code column NOT LIKE (subquery)} condition, the subquery {@code val} returning a single field. */
    public final <E extends Entity, F, R extends RelatedTo> W notLike(Field<E, F, R> column, InnerQueryFieldsProvider<? extends R, F> val) {
        temp(column, NOT_LIKE, val);
        return (W) this;
    }


    /** Adds a {@code column IN (subquery)} condition, the subquery {@code val} returning a single field. */
    public final <E extends Entity, F, R extends RelatedTo> W in(Field<E, F, R> column, InnerQueryFieldsProvider<? extends R, F> val) {
        temp(column, IN, val);
        return (W) this;
    }

    /** Adds a {@code column NOT IN (subquery)} condition, the subquery {@code val} returning a single field. */
    public final <E extends Entity, F, R extends RelatedTo> W notIn(Field<E, F, R> column, InnerQueryFieldsProvider<? extends R, F> val) {
        temp(column, NOT_IN, val);
        return (W) this;
    }

    /** Adds a {@code column = column2} condition comparing two columns. */
    public final <F> W equalColumn(Column<?, F, ?> column, Column<?, F, ?> column2) {
        temp(column, EQ, column2);
        return (W) this;
    }

    /** Adds a {@code column > column2} condition comparing two columns. */
    public final <F> W greaterColumn(Column<?, F, ?> column, Column<?, F, ?> column2) {
        temp(column, GT, column2);
        return (W) this;
    }

    /** Adds a {@code column >= column2} condition comparing two columns. */
    public final <F> W greaterOrEqualColumn(Column<?, F, ?> column, Column<?, F, ?> column2) {
        temp(column, GTEQ, column2);
        return (W) this;
    }

    /** Adds a {@code column < column2} condition comparing two columns. */
    public final <F> W lessColumn(Column<?, F, ?> column, Column<?, F, ?> column2) {
        temp(column, LT, column2);
        return (W) this;
    }

    /** Adds a {@code column <= column2} condition comparing two columns. */
    public final <F> W lessOrEqualColumn(Column<?, F, ?> column, Column<?, F, ?> column2) {
        temp(column, LTEQ, column2);
        return (W) this;
    }

    /** Adds a {@code column <> column2} condition comparing two columns. */
    public final <F> W notEqualColumn(Column<?, F, ?> column, Column<?, F, ?> column2) {
        temp(column, NOT_EQ, column2);
        return (W) this;
    }

    /** Adds equality conditions for every column of the composite key {@code key}, matched against {@code val}. */
    @SuppressWarnings("java:S1221")
    public final <K extends Composite, K1 extends Composite> W equal(CompositeColumns<K> key, K1 val) {
        temp(key.getWhere(val));
        return (W) this;
    }

    /**
     * Collects the names of every entity referenced by this clause, recursing into the nested clauses.
     *
     * @param target the target that resolves the entity names
     * @return the set of referenced entity names
     */
    public Set<String> getAllDaoInWhereClause(Target target) {
        Set<String> rv = new HashSet<>();
        for (int i = 1; i < getCounter(); i++) {
            Field<?, ?, ?> key = getKeyForPointer(i);

            Object val = getValueForPointer(i);
            if (val instanceof Where) {
                rv.addAll(((Where) val).getAllDaoInWhereClause(target));
            }
            if (key != null) rv.add(target.getEntityName(key.entityClass()));
        }

        return rv;
    }

    /** Adds an equality condition (internal shortcut for {@link Operator#EQ}). */
    private <F, R extends RelatedTo> void temp(Field<?, F, R> column, F val) {
        temp(column, Operator.EQ, val);
    }

    /** Builds a condition against an entity subquery, projecting it to the column's field before storing it. */
    private <E1 extends Entity, E2 extends Entity, F, R extends RelatedTo> void temp(Column<E2, F, R> column, Operator operator, InnerQueryEntity<E1> select) {
        if (column == null) throw new MandatoryColumn();
        if (select == null) throw new ValueCanNotBeNullException();

        Column<E1, F, R> col = new Column<E1, F, R>() {

            @Override
            public String getColumnName() {
                return column.getColumnName();
            }

            @Override
            public String getFieldName() {
                return column.getFieldName();
            }

            @Override
            public Class<F> getFieldClass() {
                return column.getFieldClass();
            }

            @Override
            public F getValue(R entity) {
                return null;
            }

            @SuppressWarnings("java:S1186")
            @Override
            public R setValue(R entity, F value) {
                return entity;
            }

            @Override
            public E1 getInstance() {
                return null;
            }

            @Override
            public Class<E1> entityClass() {
                return null;
            }
        };

        putKeyMandatoryRelationValue(column, operator, select.limitToField(col).innerResult());
    }

    /** Adds a value-less condition; only {@link Operator#IS_NULL}/{@link Operator#NOT_NULL} are allowed. */
    private <F> void temp(Field<?, F, ?> column, Operator operator) {

        if (!Operator.IS_NULL.equals(operator) && !Operator.NOT_NULL.equals(operator)) {
            throw new DaobabException("Where clause without value is allowed only for SQLOperators " + Operator.IS_NULL + "," + Operator.NOT_NULL);
        }

        if (column == null) throw new MandatoryColumn();

        put(KEY + getCounter(), column);
        put(RELATION + getCounter(), operator);
        setCounter(getCounter() + 1);
    }

    /** Adds a condition taking its value from a related entity; numeric values are flagged as buffer-indexable. */
    private <E extends Entity, F, R extends RelatedTo> void temp(Field<E, F, R> column, Operator operator, R val) {
        if (column == null) throw new MandatoryColumn();
        if (val instanceof Number) {
            put(MAY_BE_INDEXED_IN_BUFFER + getCounter(), Boolean.TRUE);
        }
        putKeyMandatoryRelationValue(column, operator, column.getValueOf(val));
    }

    /** Adds a raw condition (used to build the HAVING clause). */
    protected void tempHaving(Column column, Operator operator, Object val) {
        if (column == null) throw new MandatoryColumn();
        putKeyMandatoryRelationValue(column, operator, val);
    }

    /** Adds a condition with a plain field value; numeric values are flagged as buffer-indexable. */
    private <E extends Entity, F, R extends RelatedTo> void temp(Field<E, F, R> column, Operator operator, F val) {
        if (column == null) throw new MandatoryColumn();
        if (val instanceof Number) {
            put(MAY_BE_INDEXED_IN_BUFFER + getCounter(), Boolean.TRUE);
        }
        putKeyMandatoryRelationValue(column, operator, val);
    }

    /** Adds one condition per column of a composite key, wrapped in a nested {@code AND} group. */
    private <K extends Composite, K1 extends Composite> void temp(CompositeColumns<K> keys, Operator operator, K1 val) {
        WhereAnd whereAnd = new WhereAnd();

        for (TableColumn tableColumn : keys) {
            Column column = tableColumn.getColumn();
            temp(column, operator, column.getValue((RelatedTo) val));
        }
        temp(whereAnd);
    }

    /** Adds a collection condition over a collection of field values. */
    private <F, R extends RelatedTo> void tempColField(Field<?, F, R> column, Operator operator, Collection<? super F> val) {
        if (column == null) throw new MandatoryColumn();
        if (val == null) throw new ValueCanNotBeNullException();
        putKeyMandatoryRelationValue(column, operator, val);
    }

    /** Adds a collection condition, mapping the related entities to their field values. */
    private <F, R extends RelatedTo> void temp(Field<?, F, R> column, Operator operator, Collection<? extends R> val) {
        if (column == null) throw new MandatoryColumn();
        if (val == null) throw new ValueCanNotBeNullException();
        List<F> lisf = val.stream().map(column::getValueOf).collect(Collectors.toList());
        putKeyMandatoryRelationValue(column, operator, lisf);
    }


    /** Adds a collection condition from a vararg of field values. */
    private <F, R extends RelatedTo> void temp(Field<?, F, R> column, Operator operator, F... val) {
        if (column == null) throw new MandatoryColumn();
        if (val == null) throw new ValueCanNotBeNullException();
        List<F> lisf = Arrays.asList(val);
        putKeyMandatoryRelationValue(column, operator, lisf);
    }

    /** Adds a collection condition from a vararg of related entities, mapped to their field values. */
    private <F, R extends RelatedTo> void temp(Field<?, F, R> column, Operator operator, R... val) {
        if (column == null) throw new MandatoryColumn();
        if (val == null) throw new ValueCanNotBeNullException();
        putKeyMandatoryRelationValue(column, operator, Arrays.stream(val).map(column::getValueOf).collect(Collectors.toList()));
    }


    /** Adds a condition comparing two columns. */
    private <F> void temp(Field<?, F, ?> column, Operator operator, Field<?, F, ?> column2) {
        if (column == null) throw new MandatoryColumn();
        if (column2 == null) throw new MandatoryColumn();
        putKeyMandatoryRelationValue(column, operator, column2);
    }

    /** Builds a strictly-between condition (a nested {@code AND} of {@code >} and {@code <}) over related entities. */
    private <F, R extends RelatedTo> void tempBetween(Field<?, F, R> column, R value1, R value2) {
        if (column == null) throw new MandatoryColumn();
        if (value1 == null) throw new ValueCanNotBeNullException();
        if (value2 == null) throw new ValueCanNotBeNullException();
        WhereAnd where = new WhereAnd();
        where.greater(column, column.getValueOf(value1))
                .less(column, column.getValueOf(value2));

        temp(where);
    }

    /** Builds a strictly-between condition (a nested {@code AND} of {@code >} and {@code <}) over field values. */
    private <F, R extends RelatedTo> void tempBetween(Field<?, F, R> column, F value1, F value2) {
        if (column == null) throw new MandatoryColumn();
        if (value1 == null) throw new ValueCanNotBeNullException();
        if (value2 == null) throw new ValueCanNotBeNullException();
        WhereAnd where = new WhereAnd();
        where.greater(column, value1)
                .less(column, value2);

        temp(where);
    }


    /** Adds a nested clause (optimized first) as a wrapped sub-condition. */
    protected void temp(Where val) {
        val.optimize();
        put(WRAPPER + getCounter(), val);
        put(VALUE + getCounter(), val);
        setCounter(getCounter() + 1);
    }

    /** Adds a condition against a single-field subquery. */
    private <F, R extends RelatedTo> void temp(Field<?, F, R> column, Operator operator, InnerQueryFieldsProvider<? extends R, F> select) {
        if (column == null) throw new MandatoryColumn();
        if (operator == null) throw new NullOperator();
        if (select == null) throw new MandatoryInnerQuery();
        put(KEY + getCounter(), column);
        put(VALUE + getCounter(), select.innerResult());
        put(RELATION + getCounter(), operator);
        setCounter(getCounter() + 1);
    }

    /** Stores a column/operator/value triple, rejecting a {@code null} operator or value. */
    private void putKeyMandatoryRelationValue(Object key, Object relation, Object value) {
        if (relation == null) throw new NullOperator();
        if (value == null) throw new ValueCanNotBeNullException();
        put(KEY + getCounter(), key);
        put(RELATION + getCounter(), relation);
        put(VALUE + getCounter(), value);
        setCounter(getCounter() + 1);
    }

    /**
     * Deep-copies this clause into a new group of the same relation, optimizing the nested clauses on the way.
     *
     * @return the copy
     * @throws DaobabException if the relation between the expressions is invalid
     */
    public Where clone() {

        Where rv;
        String relation = (String) getWhereMap().get(DictRemoteKey.REL_BETWEEN_EXPRESSIONS);
        if (AND.equals(relation)) {
            rv = new WhereAnd();
        } else if (OR.equals(relation)) {
            rv = new WhereOr();
        } else if (NOT.equals(relation)) {
            rv = new WhereNot();
        } else {
            throw new DaobabException("Invalid relation: " + relation);
        }

        for (int counter = 1; counter < getCounter(); counter++) {
            Object k = getWhereMap().get(KEY + counter);
            if (k != null) rv.put(KEY + counter, k);

            Object w = getWhereMap().get(WRAPPER + counter);
            if (w != null) {
                Where where = ((Where) w);
                where.optimize();
                rv.put(WRAPPER + counter, where);
            }

            Object v = getWhereMap().get(VALUE + counter);
            if (v != null) rv.put(VALUE + counter, v);

            Object r = getWhereMap().get(RELATION + counter);
            if (r != null) rv.put(RELATION + counter, r);
        }
        return rv;
    }

    /** Whether this group's relation equals {@code relation}. */
    public boolean hasRelation(String relation) {
        return relation.equals(getRelationBetweenExpressions());
    }

}
