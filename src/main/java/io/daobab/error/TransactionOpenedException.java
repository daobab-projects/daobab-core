package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class TransactionOpenedException extends DaobabException {

    private static final long serialVersionUID = 1L;

    public TransactionOpenedException() {
        super("Transaction already opened");
    }

}
