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

    default Q groupBy(Column<?, ?, ?>... columns) {
        if (columns == null || columns.length == 0) return (Q) this;
        getGroupBy().clear();
        getGroupBy().addAll(Arrays.asList(columns));
        return (Q) this;
    }

}
