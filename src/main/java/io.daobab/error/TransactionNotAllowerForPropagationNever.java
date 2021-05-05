package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class TransactionNotAllowerForPropagationNever extends DaobabException {

    private static final long serialVersionUID = 1L;


    public TransactionNotAllowerForPropagationNever() {
        super("Transaction not allowed for propagation 'NEVER' ");
    }


}
