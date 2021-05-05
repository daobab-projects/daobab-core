package io.daobab.result.predicate;

public class MatchNotIN extends MatchIN {

    public MatchNotIN(Object valueToCompare) {
        super(valueToCompare);
    }

    public boolean test(Object valueFromEntityField) {
        return !super.test(valueFromEntityField);
    }
}
