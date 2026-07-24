package io.daobab.model;

import java.util.List;

/**
 * Something that exposes an ordered list of columns - the shared supertype of entities and column collections.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface ColumnsProvider {

    /**
     * The columns, in their declared order.
     */
    List<TableColumn> columns();

}
