package io.daobab.model;

import io.daobab.query.base.Query;
import io.daobab.query.base.QueryJoin;

import java.util.Collections;
import java.util.List;


public interface EnhancedEntity extends ColumnsProvider {

    default List<Column> joinedColumns() {
        return Collections.emptyList();
    }

    <Q extends Query & QueryJoin<Q>> Q enhanceQuery(Q query);

}
