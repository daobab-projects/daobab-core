package io.daobab.model;

import io.daobab.target.QueryTarget;

public interface IdGeneratorSupplier<F> {

    F generateId(QueryTarget currentTarget);
}
