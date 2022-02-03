package io.daobab.query.base;

import io.daobab.model.*;

import java.util.*;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
@SuppressWarnings({"unchecked","rawtypes","UnusedReturnValue","unused"})
public interface QueryGroupBy<Q extends Query> {

    List<Column<?,?,?>> getGroupBy();
    String getGroupByAlias();

    Q groupBy(String alias);

    Q groupBy(Column<?, ?, ?>... columns);

}
