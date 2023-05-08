package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class TransactionOpenedAlready extends DaobabException {

    private static final long serialVersionUID = 1L;

    public TransactionOpenedAlready() {
        super("Transaction already opened");
    }

}
