package io.daobab.result.predicate;

public class MatchNotEQ extends MatchEQ {

    public MatchNotEQ(Object valueToCompare) {
        super(valueToCompare);
    }

    @Override
    public boolean test(Object valueFromEntityField) {
        return !super.test(valueFromEntityField);
    }
}
