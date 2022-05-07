package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class TransactionAlreadyOpened extends DaobabException {

    private static final long serialVersionUID = 1L;

    public TransactionAlreadyOpened() {
        super("Transaction already opened");
    }

}
