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
import io.daobab.target.buffer.single.Entities;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static io.daobab.statement.condition.Operator.*;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
@SuppressWarnings({"unchecked", "rawtypes", "unused", "UnusedReturnValue"})
public abstract class Where<W extends Where> extends WhereBase {

    public boolean isEmpty() {
        return this.getCounter() == 1;
    }

    public final W ifTrue(boolean enabled, UnaryOperator<W> condition) {
        if (enabled) {
            return condition.apply((W) this);
        }
        return (W) this;
    }

    public final W ifFalse(boolean enabled, UnaryOperator<W> condition) {
        if (!enabled) {
            return condition.apply((W) this);
        }
        return (W) this;
    }

    public final W ifElse(boolean enabled, UnaryOperator<W> ifTrue, UnaryOperator<W> ifFalse) {
        if (enabled) {
            return ifTrue.apply((W) this);
        } else {
            return ifFalse.apply((W) this);
        }
    }

    public final <X> W ifTrue(Predicate<X> predicate, X val, UnaryOperator<W> condition) {
        if (predicate.test(val)) {
            return condition.apply((W) this);
        }
        return (W) this;
    }

    public final <X> W ifFalse(Predicate<X> predicate, X val, UnaryOperator<W> condition) {
        if (!predicate.test(val)) {
            return condition.apply((W) this);
        }
        return (W) this;
    }

    public final <X> W ifElse(Predicate<X> predicate, X val, UnaryOperator<W> ifTrue, UnaryOperator<W> ifFalse) {
        if (predicate.test(val)) {
            return ifTrue.apply((W) this);
        } else {
            return ifFalse.apply((W) this);
        }
    }


    public final <E extends Entity, F, R extends EntityRelation> W ifNotNull(Field<E, F, R> column, F val) {
        if (val != null) temp(column, val);
        return (W) this;
    }


    public final <E extends Entity, F, R extends EntityRelation> W ifNotNull(Field<E, F, R> column, Operator operator, F val) {
        if (val != null) temp(column, operator, val);
        return (W) this;
    }


    public final <F> W isNull(Field<?, F, ?> column) {
        temp(column, IS_NULL);
        return (W) this;
    }

    public final <F> W notNull(Field<?, F, ?> column) {
        temp(column, NOT_NULL);
        return (W) this;
    }

    @SuppressWarnings("java:S1221")
    public final <E extends Entity, F, R extends EntityRelation> W equal(Field<E, F, R> column, F val) {
        temp(column, EQ, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W greater(Field<E, F, R> column, F val) {
        temp(column, GT, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W greaterOrEqual(Field<E, F, R> column, F val) {
        temp(column, GTEQ, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W less(Field<E, F, R> column, F val) {
        temp(column, LT, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W lessOrEqual(Field<E, F, R> column, F val) {
        temp(column, LTEQ, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W notEqual(Field<E, F, R> column, F val) {
        temp(column, NOT_EQ, val);
        return (W) this;
    }

    @SuppressWarnings("java:S1221")
    public final <E extends Entity, F, R extends EntityRelation> W equal(Field<E, F, R> column, R val) {
        temp(column, EQ, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W greater(Field<E, F, R> column, R val) {
        temp(column, GT, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W greaterOrEqual(Field<E, F, R> column, R val) {
        temp(column, GTEQ, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W less(Field<E, F, R> column, R val) {
        temp(column, LT, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W lessOrEqual(Field<E, F, R> column, R val) {
        temp(column, LTEQ, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W notEqual(Field<E, F, R> column, R val) {
        temp(column, NOT_EQ, val);
        return (W) this;
    }

    @SuppressWarnings("java:S1221")
    public final <E extends Entity, F, R extends EntityRelation> W equal(Field<E, F, R> column, Field<?, F, ?> val) {
        temp(column, EQ, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W greater(Field<E, F, R> column, Field<?, F, ?> val) {
        temp(column, GT, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W greaterOrEqual(Field<E, F, R> column, Field<?, F, ?> val) {
        temp(column, GTEQ, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W less(Field<E, F, R> column, Field<?, F, ?> val) {
        temp(column, LT, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W lessOrEqual(Field<E, F, R> column, Field<?, F, ?> val) {
        temp(column, LTEQ, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W notEqual(Field<E, F, R> column, Field<?, F, ?> val) {
        temp(column, NOT_EQ, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W like(Field<E, F, R> column, F val) {
        temp(column, LIKE, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W notLike(Field<E, F, R> column, F val) {
        temp(column, NOT_LIKE, val);
        return (W) this;
    }


    public final <E extends Entity, F, R extends EntityRelation> W in(Field<E, F, R> column, F... val) {
        temp(column, IN, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W notIn(Field<E, F, R> column, F... val) {
        temp(column, NOT_IN, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W in(Field<E, F, R> column, R... val) {
        temp(column, IN, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W notIn(Field<E, F, R> column, R... val) {
        temp(column, NOT_IN, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W inFields(Field<E, F, R> column, Collection<F> val) {
        tempColField(column, IN, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W notInFields(Field<E, F, R> column, Collection<F> val) {
        tempColField(column, NOT_IN, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W in(Field<E, F, R> column, Entities<? extends R> val) {
        temp(column, IN, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W notIn(Field<E, F, R> column, Entities<? extends R> val) {
        temp(column, NOT_IN, val);
        return (W) this;
    }


    public final <F> W between(Field<?, F, ?> column, F valueFrom, F valueTo) {
        tempBetween(column, valueFrom, valueTo);
        return (W) this;
    }


    public final <F, R extends EntityRelation> W between(Field<?, F, R> column, R valueFrom, R valueTo) {
        tempBetween(column, valueFrom, valueTo);
        return (W) this;
    }

    @SuppressWarnings("java:S1221")
    public final <E extends Entity, F, R extends EntityRelation> W equal(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        temp(column, EQ, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W greater(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        temp(column, GT, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W greaterOrEqual(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        temp(column, GTEQ, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W less(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        temp(column, LT, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W lessOrEqual(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        temp(column, LTEQ, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W notEqual(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        temp(column, NOT_EQ, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W like(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        temp(column, LIKE, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W notLike(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        temp(column, NOT_LIKE, val);
        return (W) this;
    }


    public final <E extends Entity, F, R extends EntityRelation> W in(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        temp(column, IN, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W notIn(Column<E, F, R> column, InnerQueryEntity<? extends R> val) {
        temp(column, NOT_IN, val);
        return (W) this;
    }


    @SuppressWarnings("java:S1221")
    public final <E extends Entity, F, R extends EntityRelation> W equal(Field<E, F, R> column, InnerQueryFieldsProvider<? extends R, F> val) {
        temp(column, EQ, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W greater(Field<E, F, R> column, InnerQueryFieldsProvider<? extends R, F> val) {
        temp(column, GT, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W greaterOrEqual(Field<E, F, R> column, InnerQueryFieldsProvider<? extends R, F> val) {
        temp(column, GTEQ, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W less(Field<E, F, R> column, InnerQueryFieldsProvider<? extends R, F> val) {
        temp(column, LT, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W lessOrEqual(Field<E, F, R> column, InnerQueryFieldsProvider<? extends R, F> val) {
        temp(column, LTEQ, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W notEqual(Field<E, F, R> column, InnerQueryFieldsProvider<? extends R, F> val) {
        temp(column, NOT_EQ, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W like(Field<E, F, R> column, InnerQueryFieldsProvider<? extends R, F> val) {
        temp(column, LIKE, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W notLike(Field<E, F, R> column, InnerQueryFieldsProvider<? extends R, F> val) {
        temp(column, NOT_LIKE, val);
        return (W) this;
    }


    public final <E extends Entity, F, R extends EntityRelation> W in(Field<E, F, R> column, InnerQueryFieldsProvider<? extends R, F> val) {
        temp(column, IN, val);
        return (W) this;
    }

    public final <E extends Entity, F, R extends EntityRelation> W notIn(Field<E, F, R> column, InnerQueryFieldsProvider<? extends R, F> val) {
        temp(column, NOT_IN, val);
        return (W) this;
    }

    public final <F> W equalColumn(Column<?, F, ?> column, Column<?, F, ?> column2) {
        temp(column, EQ, column2);
        return (W) this;
    }

    public final <F> W greaterColumn(Column<?, F, ?> column, Column<?, F, ?> column2) {
        temp(column, GT, column2);
        return (W) this;
    }

    public final <F> W greaterOrEqualColumn(Column<?, F, ?> column, Column<?, F, ?> column2) {
        temp(column, GTEQ, column2);
        return (W) this;
    }

    public final <F> W lessColumn(Column<?, F, ?> column, Column<?, F, ?> column2) {
        temp(column, LT, column2);
        return (W) this;
    }

    public final <F> W lessOrEqualColumn(Column<?, F, ?> column, Column<?, F, ?> column2) {
        temp(column, LTEQ, column2);
        return (W) this;
    }

    public final <F> W notEqualColumn(Column<?, F, ?> column, Column<?, F, ?> column2) {
        temp(column, NOT_EQ, column2);
        return (W) this;
    }

    @SuppressWarnings("java:S1221")
    public final <K extends Composite<?>> W equal(CompositeColumns key, K val) {
        temp(key, EQ, val);
        return (W) this;
    }

    public final <K extends Composite<?>> W notEqual(CompositeColumns key, K val) {
        temp(key, NOT_EQ, val);
        return (W) this;
    }

    public Set<String> getAllDaoInWhereClause() {
        Set<String> rv = new HashSet<>();
        for (int i = 1; i < getCounter(); i++) {
            Field<?, ?, ?> key = getKeyForPointer(i);

            Object val = getValueForPointer(i);
            if (val instanceof Where) {
                rv.addAll(((Where) val).getAllDaoInWhereClause());
            }
            if (key != null) rv.add(key.getEntityName());
        }

        return rv;
    }

    private <F, R extends EntityRelation> void temp(Field<?, F, R> column, F val) {
        temp(column, Operator.EQ, val);
    }

    private <E1 extends Entity, E2 extends Entity, F, R extends EntityRelation> void temp(Column<E2, F, R> column, Operator operator, InnerQueryEntity<E1> select) {
        if (column == null) throw new ColumnMandatory();
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
            public String getEntityName() {
                return select.getEntityName();
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
            public void setValue(R entity, F value) {
            }

            @Override
            public E1 getInstance() {
                return null;
            }

            @Override
            public Class<E1> getEntityClass() {
                return null;
            }
        };

        putKeyMandatoryRelationValue(column, operator, select.limitToField(col).innerResult());
    }

    private <F> void temp(Field<?, F, ?> column, Operator operator) {

        if (!Operator.IS_NULL.equals(operator) && !Operator.NOT_NULL.equals(operator)) {
            throw new DaobabException("Where clause without value is allowed only for SQLOperators " + Operator.IS_NULL + "," + Operator.NOT_NULL);
        }

        if (column == null) throw new ColumnMandatory();

        put(KEY + getCounter(), column);
        put(RELATION + getCounter(), operator);
        setCounter(getCounter() + 1);
    }

    private <E extends Entity, F, R extends EntityRelation> void temp(Field<E, F, R> column, Operator operator, R val) {
        if (column == null) throw new ColumnMandatory();
        if (val instanceof Number) {
            put(MAY_BE_INDEXED_IN_BUFFER + getCounter(), Boolean.TRUE);
        }
        putKeyMandatoryRelationValue(column, operator, column.getValueOf(val));
    }

    protected void tempHaving(Column column, Operator operator, Object val) {
        if (column == null) throw new ColumnMandatory();
        putKeyMandatoryRelationValue(column, operator, val);
    }

    private <E extends Entity, F, R extends EntityRelation> void temp(Field<E, F, R> column, Operator operator, F val) {
        if (column == null) throw new ColumnMandatory();
        if (val instanceof Number) {
            put(MAY_BE_INDEXED_IN_BUFFER + getCounter(), Boolean.TRUE);
        }
        putKeyMandatoryRelationValue(column, operator, val);
    }

    private <K extends Composite> void temp(CompositeColumns<K> keys, Operator operator, K val) {
        WhereAnd whereAnd = new WhereAnd();

        for (TableColumn tcol : keys) {
            Column col = tcol.getColumn();
            temp(col, operator, col.getValue((EntityRelation) val));
        }
        temp(whereAnd);
    }

    private <F, R extends EntityRelation> void tempColField(Field<?, F, R> column, Operator operator, Collection<F> val) {
        if (column == null) throw new ColumnMandatory();
        if (val == null) throw new ValueCanNotBeNullException();
        putKeyMandatoryRelationValue(column, operator, val);
    }

    private <F, R extends EntityRelation> void temp(Field<?, F, R> column, Operator operator, Collection<? extends R> val) {
        if (column == null) throw new ColumnMandatory();
        if (val == null) throw new ValueCanNotBeNullException();
        List<F> lisf = val.stream().map(column::getValueOf).collect(Collectors.toList());
        putKeyMandatoryRelationValue(column, operator, lisf);
    }


    private <F, R extends EntityRelation> void temp(Field<?, F, R> column, Operator operator, F... val) {
        if (column == null) throw new ColumnMandatory();
        if (val == null) throw new ValueCanNotBeNullException();
        List<F> lisf = Arrays.asList(val);
        putKeyMandatoryRelationValue(column, operator, lisf);
    }

    private <F, R extends EntityRelation> void temp(Field<?, F, R> column, Operator operator, R... val) {
        if (column == null) throw new ColumnMandatory();
        if (val == null) throw new ValueCanNotBeNullException();
        putKeyMandatoryRelationValue(column, operator, Arrays.stream(val).map(column::getValueOf).collect(Collectors.toList()));
    }


    private <F> void temp(Field<?, F, ?> column, Operator operator, Field<?, F, ?> column2) {
        if (column == null) throw new ColumnMandatory();
        if (column2 == null) throw new ColumnMandatory();
        putKeyMandatoryRelationValue(column, operator, column2);
    }

    private <F, R extends EntityRelation> void tempBetween(Field<?, F, R> column, R value1, R value2) {
        if (column == null) throw new ColumnMandatory();
        if (value1 == null) throw new ValueCanNotBeNullException();
        if (value2 == null) throw new ValueCanNotBeNullException();
        WhereAnd where = new WhereAnd();
        where.greater(column, column.getValueOf(value1))
                .less(column, column.getValueOf(value2));

        temp(where);
    }

    private <F, R extends EntityRelation> void tempBetween(Field<?, F, R> column, F value1, F value2) {
        if (column == null) throw new ColumnMandatory();
        if (value1 == null) throw new ValueCanNotBeNullException();
        if (value2 == null) throw new ValueCanNotBeNullException();
        WhereAnd where = new WhereAnd();
        where.greater(column, value1)
                .less(column, value2);

        temp(where);
    }


    protected void temp(Where val) {
        val.optimize();
        put(WRAPPER + getCounter(), val);
        put(VALUE + getCounter(), val);
        setCounter(getCounter() + 1);
    }

    private <F, R extends EntityRelation> void temp(Field<?, F, R> column, Operator operator, InnerQueryFieldsProvider<? extends R, F> select) {
        if (column == null) throw new ColumnMandatory();
        if (operator == null) throw new NullOperator();
        if (select == null) throw new InnerQueryMandatory();
        put(KEY + getCounter(), column);
        put(VALUE + getCounter(), select.innerResult());
        put(RELATION + getCounter(), operator);
        setCounter(getCounter() + 1);
    }

    private void putKeyMandatoryRelationValue(Object key, Object relation, Object value) {
        if (relation == null) throw new NullOperator();
        if (value == null) throw new ValueCanNotBeNullException();
        put(KEY + getCounter(), key);
        put(RELATION + getCounter(), relation);
        put(VALUE + getCounter(), value);
        setCounter(getCounter() + 1);
    }

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

    public boolean hasRelation(String relation) {
        return relation.equals(getRelationBetweenExpressions());
    }

}
