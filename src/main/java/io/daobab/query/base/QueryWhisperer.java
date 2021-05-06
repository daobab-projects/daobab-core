package io.daobab.query.base;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.statement.condition.Count;
import io.daobab.statement.condition.Having;
import io.daobab.statement.condition.Order;
import io.daobab.statement.condition.SetField;
import io.daobab.statement.where.WhereAnd;
import io.daobab.statement.where.WhereNot;
import io.daobab.statement.where.WhereOr;
import io.daobab.statement.where.base.Where;

@SuppressWarnings({"unchecked","rawtypes","UnusedReturnValue","unused"})
public interface QueryWhisperer {

    default WhereAnd and() {
        return new WhereAnd();
    }

    default Having having() {
        return new Having();
    }

    default WhereOr or() {
        return new WhereOr();
    }

    default WhereOr or(Where... where) {
        return new WhereOr(where);
    }

    default WhereAnd and(Where... where) {
        return new WhereAnd(where);
    }

    default WhereNot not() {
        return new WhereNot();
    }

    default Order asc(Column<?, ?, ?> col) {
        Order o = new Order();
        return o.asc(col);
    }

    default Order desc(Column<?, ?, ?> col) {
        Order o = new Order();
        return o.desc(col);
    }

    default <E1 extends Entity, E2 extends Entity, F> JoinOn<E1, E2, F> joinOn(Column<E1, F, ?> left, Column<E2, F, ?> right) {
        return new JoinOn<>(left, right);
    }

    default Count fieldDistinct(Column<?, ?, ?> col) {
        return Count.fieldDistinct(col);
    }

    default <E extends Entity, F> SetField<E> set(Column<E, F, ?> col, F val) {
        return new SetField(col, val);
    }

}
