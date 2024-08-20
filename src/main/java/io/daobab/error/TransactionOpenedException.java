package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class TransactionOpenedException extends DaobabException {

    public TransactionOpenedException() {
        super("Transaction already opened");
    }

}
