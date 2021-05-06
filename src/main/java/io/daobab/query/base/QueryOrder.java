package io.daobab.query.base;

import io.daobab.model.Column;
import io.daobab.statement.condition.Order;

import java.util.function.Supplier;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
@SuppressWarnings({"rawtypes","UnusedReturnValue","unused"})
public interface QueryOrder<Q extends Query> {

    Q orderBy(Order orderBy);


    default Q orderBy(Supplier<Order> order) {
        return orderBy(order.get());
    }

    default Q orderDescBy(Column<?, ?, ?> key) {
        return orderBy(new Order().desc(key));
    }

    default Q orderAscBy(Column<?, ?, ?> key) {
        return orderBy(new Order().asc(key));
    }

    default Q orderBy(Column<?, ?, ?> key) {
        return orderBy(new Order().asc(key));
    }

}
