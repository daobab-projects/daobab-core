package io.daobab.target;

/**
 * A marker for a {@link Target} able to execute queries. It declares no methods; it is used as an intersection
 * bound ({@code T extends Target & QueryHandler}) so that the entity lifecycle hooks and CRUD operations only
 * accept a query-capable target.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface QueryHandler {
}
