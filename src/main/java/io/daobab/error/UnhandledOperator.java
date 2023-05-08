package io.daobab.error;

import io.daobab.statement.condition.Operator;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class UnhandledOperator extends DaobabException {

    private static final long serialVersionUID = 1L;

    public UnhandledOperator(Operator operator) {
        super("Unhandled Operator: " + operator);
    }

}
