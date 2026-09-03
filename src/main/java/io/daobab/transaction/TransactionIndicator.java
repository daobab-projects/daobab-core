package io.daobab.transaction;

public enum TransactionIndicator {

    EXECUTE_WITHOUT,
    START_NEW_JUST_FOR_IT,
    GO_AHEAD,
    /**
     * Go ahead on the transaction that is already open, but inside a nested transaction of its own: the work is
     * guarded by a savepoint, so failing it rolls back the work alone and leaves the enclosing transaction open.
     */
    GO_AHEAD_NESTED

}
