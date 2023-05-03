package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class TransactionNotAllowerForPropagationNever extends DaobabException {

    private static final long serialVersionUID = 1L;

    public TransactionNotAllowerForPropagationNever() {
        super("Transaction not allowed for propagation 'NEVER' ");
    }

}
