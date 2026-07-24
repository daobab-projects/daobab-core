package io.daobab.model;

import io.daobab.query.base.Query;
import io.daobab.query.base.QueryJoin;

import java.util.Collections;
import java.util.List;

/**
 * An entity that enriches the query it takes part in - contributing extra joined columns
 * ({@link #joinedColumns()}) and adding joins/conditions to the query ({@link #enhanceQuery(Query)}). The basis
 * of the "enhanced entity" pattern, e.g. a view-like entity spanning several joined tables.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings("rawtypes")
public interface EnhancedEntity extends ColumnsProvider {

    /**
     * The extra columns this entity contributes to the query (empty by default).
     */
    default List<Column> joinedColumns() {
        return Collections.emptyList();
    }

    /** Adds this entity's joins/conditions to the query and returns it. */
    <Q extends Query & QueryJoin<Q>> Q enhanceQuery(Q query);

}
