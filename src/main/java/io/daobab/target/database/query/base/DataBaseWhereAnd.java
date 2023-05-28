package io.daobab.target.database.query.base;

import io.daobab.error.MandatoryColumn;
import io.daobab.error.NullOperator;
import io.daobab.error.ValueCanNotBeNullException;
import io.daobab.model.Entity;
import io.daobab.model.Field;
import io.daobab.model.RelatedTo;
import io.daobab.statement.condition.Operator;
import io.daobab.statement.where.WhereAnd;
import io.daobab.target.database.query.frozen.DaoParam;

import static io.daobab.statement.condition.Operator.*;

@SuppressWarnings("rawtypes")
public class DataBaseWhereAnd extends WhereAnd {


    @SuppressWarnings("java:S1221")
    public final <E extends Entity, F, R extends RelatedTo> DataBaseWhereAnd equal(Field<E, F, R> column, DaoParam val) {
        temp(column, EQ, val);
        return this;
    }

    public final <E extends Entity, F, R extends RelatedTo> DataBaseWhereAnd greater(Field<E, F, R> column, DaoParam val) {
        temp(column, GT, val);
        return this;
    }

    public final <E extends Entity, F, R extends RelatedTo> DataBaseWhereAnd greaterOrEqual(Field<E, F, R> column, DaoParam val) {
        temp(column, GTEQ, val);
        return this;
    }

    public final <E extends Entity, F, R extends RelatedTo> DataBaseWhereAnd less(Field<E, F, R> column, DaoParam val) {
        temp(column, LT, val);
        return this;
    }

    public final <E extends Entity, F, R extends RelatedTo> DataBaseWhereAnd lessOrEqual(Field<E, F, R> column, DaoParam val) {
        temp(column, LTEQ, val);
        return this;
    }

    public final <E extends Entity, F, R extends RelatedTo> DataBaseWhereAnd notEqual(Field<E, F, R> column, DaoParam val) {
        temp(column, NOT_EQ, val);
        return this;
    }


    public final <E extends Entity, F, R extends RelatedTo> DataBaseWhereAnd like(Field<E, F, R> column, DaoParam val) {
        temp(column, LIKE, val);
        return this;
    }

    public final <E extends Entity, F, R extends RelatedTo> DataBaseWhereAnd notLike(Field<E, F, R> column, DaoParam val) {
        temp(column, NOT_LIKE, val);
        return this;
    }

    public final <E extends Entity, F, R extends RelatedTo> DataBaseWhereAnd inFieldsParam(Field<E, F, R> column, DaoParam val) {
        tempColField(column, IN, val);
        return this;
    }

    public final <E extends Entity, F, R extends RelatedTo> DataBaseWhereAnd notInFieldsParam(Field<E, F, R> column, DaoParam val) {
        tempColField(column, NOT_IN, val);
        return this;
    }

    public final <F> DataBaseWhereAnd between(Field<?, F, ?> column, DaoParam valueFrom, DaoParam valueTo) {
        valueFrom.setAccessibleType(column.getFieldClass());
        valueTo.setAccessibleType(column.getFieldClass());
        tempBetween(column, valueFrom, valueTo);
        return this;
    }

    public final <E extends Entity, F, R extends RelatedTo> DataBaseWhereAnd in(Field<E, F, R> column, DaoParam val) {
        val.setCollection(true);
        temp(column, IN, val);
        return this;
    }

    public final <E extends Entity, F, R extends RelatedTo> DataBaseWhereAnd notIn(Field<E, F, R> column, DaoParam val) {
        val.setCollection(true);
        temp(column, NOT_IN, val);
        return this;
    }


    private <E extends Entity, F, R extends RelatedTo> void temp(Field<E, F, R> column, Operator operator, DaoParam val) {
        val.setAccessibleType(column.getFieldClass());
        if (column == null) throw new MandatoryColumn();
        putKeyMandatoryRelationValue(column, operator, val);
    }

    private void putKeyMandatoryRelationValue(Object key, Object relation, Object value) {
        if (relation == null) throw new NullOperator();
        if (value == null) throw new ValueCanNotBeNullException();
        put(KEY + getCounter(), key);
        put(RELATION + getCounter(), relation);
        put(VALUE + getCounter(), value);
        setCounter(getCounter() + 1);
    }

    private <F, R extends RelatedTo> void tempColField(Field<?, F, R> column, Operator operator, DaoParam val) {
        if (column == null) throw new MandatoryColumn();
        if (val == null) throw new ValueCanNotBeNullException();
        val.setCollection(true);
        val.setAccessibleType(column.getFieldClass());
        putKeyMandatoryRelationValue(column, operator, val);
    }

    private <F, R extends RelatedTo> void tempBetween(Field<?, F, R> column, DaoParam value1, DaoParam value2) {
        if (column == null) throw new MandatoryColumn();
        if (value1 == null) throw new ValueCanNotBeNullException();
        if (value2 == null) throw new ValueCanNotBeNullException();
        DataBaseWhereAnd where = new DataBaseWhereAnd();
        where.greater(column, value1)
                .less(column, value2);
        temp(where);
    }

}
