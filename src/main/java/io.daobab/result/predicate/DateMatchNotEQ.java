package io.daobab.result.predicate;

public class DateMatchNotEQ extends DateMatchEQ {


    public DateMatchNotEQ(Object valueToCompare) {
        super(valueToCompare);
    }


    public boolean test(Object valueFromEntityField) {
        return !super.test(valueFromEntityField);
    }
}
