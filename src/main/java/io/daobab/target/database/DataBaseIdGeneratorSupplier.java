package io.daobab.target.database;


public interface DataBaseIdGeneratorSupplier<F> {

    F generateId(QueryTarget currentTarget);
}
