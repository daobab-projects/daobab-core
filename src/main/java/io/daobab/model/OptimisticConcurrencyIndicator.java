package io.daobab.model;

import io.daobab.target.database.QueryTarget;


/**
 * An optimistic-concurrency hook for tables without a primary key: override {@link #handleOCC} to check and bump
 * a version column before an update. The default is a no-op.
 *
 * @param <E> the entity type
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
// OCC indicator for no PrimaryKey tables
public interface OptimisticConcurrencyIndicator<E extends Entity> {

    /**
     * Handles optimistic concurrency for the entity about to be updated (no-op by default; override it).
     */
    //Override this method with your own implementation
    default E handleOCC(QueryTarget target, E entityToUpdate) {
        return entityToUpdate;
    }
}
