package io.daobab.result.predicate;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class MatchIsNull extends MatchEQ {

    public MatchIsNull() {
        super(null);
    }

    @Override
    public boolean test(Object valueFromEntityField) {
        return valueFromEntityField == null;
    }
}
