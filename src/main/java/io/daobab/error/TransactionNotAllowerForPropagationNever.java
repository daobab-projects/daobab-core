package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class TransactionNotAllowerForPropagationNever extends DaobabException {

    public TransactionNotAllowerForPropagationNever() {
        super("Transaction not allowed for propagation 'NEVER' ");
    }

}
