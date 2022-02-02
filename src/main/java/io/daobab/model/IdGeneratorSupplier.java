package io.daobab.model;

import io.daobab.target.database.QueryTarget;

public interface IdGeneratorSupplier<F> {

    F generateId(QueryTarget currentTarget);
}
