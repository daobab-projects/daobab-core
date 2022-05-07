package io.daobab.error;

import io.daobab.target.Target;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class TransactionClosedException extends DaobabException {

    private static final long serialVersionUID = 1L;

    public TransactionClosedException(Target target) {
        super("Transaction is closed for " + target.getClass().getName());
    }

}
