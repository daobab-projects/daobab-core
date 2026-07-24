package io.daobab.model;

/**
 * Implemented by entities whose table name is computed at runtime rather than fixed by
 * {@link TableInformation#name()} (enabled with {@link TableInformation#useMethod()}).
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface TableNameMethod {

    /**
     * The runtime table name.
     */
    String tableName();
}
