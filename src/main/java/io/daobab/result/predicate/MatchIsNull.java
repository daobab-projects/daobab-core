package io.daobab.result.predicate;


public class MatchIsNull extends MatchEQ {

    public MatchIsNull() {
        super(null);
    }

    @Override
    public boolean test(Object valueFromEntityField) {
        return valueFromEntityField == null;
    }
}
