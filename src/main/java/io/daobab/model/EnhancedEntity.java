package io.daobab.model;

import io.daobab.query.base.Query;

import java.util.Collections;
import java.util.List;


public interface EnhancedEntity extends ColumnsProvider {

    default List<Column> joinedColumns() {
        return Collections.emptyList();
    }

    <Q extends Query> Q enhanceQuery(Q query);

}
