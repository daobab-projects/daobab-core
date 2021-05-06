package io.daobab.result.predicate;


public class MatchIsNull extends MatchEQ {


    public MatchIsNull() {
        super(null);
    }


    public boolean test(Object valueFromEntityField) {
        return valueFromEntityField == null;
    }
}
