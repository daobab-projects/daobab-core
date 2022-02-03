package io.daobab.target.buffer.transaction;

import io.daobab.target.buffer.BufferQueryTarget;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public interface OpenedTransactionBufferTarget extends BufferQueryTarget {

    void commit();

    void rollback();
}
