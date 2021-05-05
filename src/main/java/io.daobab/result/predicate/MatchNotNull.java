package io.daobab.result.predicate;


public class MatchNotNull extends MatchEQ {


    public MatchNotNull() {
        super(null);
    }


    public boolean test(Object valueFromEntityField) {
        return valueFromEntityField != null;
    }
}
