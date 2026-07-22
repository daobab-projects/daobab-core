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

/**
 * A mix-in of the little factory methods ("whispers") that read fluently inside a query: {@code and()} /
 * {@code or()} / {@code not()} for where clauses, {@code asc()} / {@code desc()} for ordering, {@code joinOn()}
 * for joins, {@code set()} for updates and {@code having()} for aggregates. The generated {@code Tables}
 * interfaces extend it, so these read naturally next to the {@code tabXxx} fields:
 * <pre>{@code
 * db.select(tabCustomer)
 *   .where(and()
 *       .equal(tabCustomer.colActive(), true)
 *       .equal(tabCustomer.colLastName(), "WILSON"))
 *   .orderBy(asc(tabCustomer.colLastName()))
 *   .findMany();
 * }</pre>
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings({"unchecked", "rawtypes", "UnusedReturnValue", "unused"})
public interface QueryWhisperer {

    /**
     * Starts a new empty {@code AND} clause.
     */
    default WhereAnd and() {
        return new WhereAnd();
    }

    /** Starts a new empty {@code HAVING} clause. */
    default Having having() {
        return new Having();
    }

    /** Starts a new empty {@code OR} clause. */
    default WhereOr or() {
        return new WhereOr();
    }

    /** Starts an {@code OR} clause seeded with the given sub-clauses. */
    default WhereOr or(Where... where) {
        return new WhereOr(where);
    }

    /** Starts an {@code AND} clause seeded with the given sub-clauses. */
    default WhereAnd and(Where... where) {
        return new WhereAnd(where);
    }

    /** Starts a new empty {@code NOT} clause. */
    default WhereNot not() {
        return new WhereNot();
    }

    /** An ascending {@link Order} by the column. */
    default Order asc(Column<?, ?, ?> col) {
        Order o = new Order();
        return o.asc(col);
    }

    /** A descending {@link Order} by the column. */
    default Order desc(Column<?, ?, ?> col) {
        Order o = new Order();
        return o.desc(col);
    }

    /** A join predicate {@code left = right}. */
    default <E1 extends Entity, E2 extends Entity, F> JoinOn<E1, E2, F> joinOn(Column<E1, F, ?> left, Column<E2, F, ?> right) {
        return new JoinOn<>(left, right);
    }

    /** A {@code COUNT(DISTINCT column)} aggregate. */
    default Count fieldDistinct(Column<?, ?, ?> col) {
        return Count.fieldDistinct(col);
    }

    /** A {@code column = value} assignment, for an update or an insert. */
    default <E extends Entity, F> SetField<E> set(Column<E, F, ?> col, F val) {
        return new SetField(col, val);
    }

}
