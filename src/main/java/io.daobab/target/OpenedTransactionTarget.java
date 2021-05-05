package io.daobab.target;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public interface OpenedTransactionTarget extends QueryTarget {

    void commit();

    void rollback();
}
