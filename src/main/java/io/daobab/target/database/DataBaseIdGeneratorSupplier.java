package io.daobab.target.database;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */

public interface DataBaseIdGeneratorSupplier<F> {

    F generateId(QueryTarget currentTarget);
}
