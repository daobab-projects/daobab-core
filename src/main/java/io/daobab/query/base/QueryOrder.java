package io.daobab.query.base;

import io.daobab.model.Column;
import io.daobab.statement.condition.Order;

import java.util.function.Supplier;

/**
 * The {@code ORDER BY} fragment of a query. The {@code orderAscBy}/{@code orderDescBy} shortcuts order by a
 * single column (or an alias); pass a built {@link Order} for a multi-column ordering. For example:
 * <pre>{@code
 * db.select(tabCustomer)
 *   .orderAscBy(tabCustomer.colLastName())
 *   .findMany();
 * }</pre>
 *
 * @param <Q> the concrete query type, returned for chaining
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings({"rawtypes", "UnusedReturnValue", "unused"})
public interface QueryOrder<Q extends Query> {

    /**
     * Orders by the given (possibly multi-column) {@link Order}.
     */
    Q orderBy(Order orderBy);

    /** Orders by the {@link Order} supplied lazily. */
    default Q orderBy(Supplier<Order> order) {
        return orderBy(order.get());
    }

    /** Orders by the column, descending. */
    default Q orderDescBy(Column<?, ?, ?> key) {
        return orderBy(new Order().desc(key));
    }

    /** Orders by the column, ascending. */
    default Q orderAscBy(Column<?, ?, ?> key) {
        return orderBy(new Order().asc(key));
    }

    /** Orders by the column, ascending (alias of {@link #orderAscBy(Column)}). */
    default Q orderBy(Column<?, ?, ?> key) {
        return orderBy(new Order().asc(key));
    }

    /** Orders by the named column/alias, descending. */
    default Q orderDescBy(String key) {
        return orderBy(new Order().desc(key));
    }

    /** Orders by the named column/alias, ascending. */
    default Q orderAscBy(String key) {
        return orderBy(new Order().asc(key));
    }

    /** Orders by the named column/alias, ascending (alias of {@link #orderAscBy(String)}). */
    default Q orderBy(String key) {
        return orderBy(new Order().asc(key));
    }

}
