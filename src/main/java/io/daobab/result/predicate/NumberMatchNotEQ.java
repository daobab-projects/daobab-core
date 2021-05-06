package io.daobab.result.predicate;

public class NumberMatchNotEQ extends NumberMatchEQ {


    public NumberMatchNotEQ(Object valueToCompare) {
        super(valueToCompare);
    }


    public boolean test(Object valueFromEntityField) {
        return !super.test(valueFromEntityField);
    }
}
