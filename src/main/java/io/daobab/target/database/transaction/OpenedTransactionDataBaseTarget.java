package io.daobab.target.database.transaction;

import io.daobab.target.database.QueryTarget;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public interface OpenedTransactionDataBaseTarget extends QueryTarget {

    void commit();

    void rollback();
}
