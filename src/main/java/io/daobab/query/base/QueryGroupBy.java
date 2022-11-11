package io.daobab.query.base;

import io.daobab.model.Column;

import java.util.List;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
@SuppressWarnings({"rawtypes", "UnusedReturnValue", "unused"})
public interface QueryGroupBy<Q extends Query> {

    List<Column<?, ?, ?>> getGroupBy();

    String getGroupByAlias();

    Q groupBy(String alias);

    Q groupBy(Column<?, ?, ?>... columns);

}
