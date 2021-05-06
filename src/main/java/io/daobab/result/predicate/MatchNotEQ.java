package io.daobab.result.predicate;

public class MatchNotEQ extends MatchEQ {


    public MatchNotEQ(Object valueToCompare) {
        super(valueToCompare);
    }


    public boolean test(Object valueFromEntityField) {
        return !super.test(valueFromEntityField);
    }
}
