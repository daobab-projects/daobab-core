package io.daobab.model;

import io.daobab.target.QueryTarget;


// OCC indicator for no PrimaryKey tables

public interface OptimisticConcurrencyIndicator<E extends Entity> {

    //Override this method with your own implementation

    default E handleOCC(QueryTarget target, E entityToUpdate) {
        return entityToUpdate;
    }
}
