package io.daobab.result.predicate;

public class TimestampMatchNotEQ extends TimestampMatchEQ {


    public TimestampMatchNotEQ(Object valueToCompare) {
        super(valueToCompare);
    }


    public boolean test(Object valueFromEntityField) {
        return !super.test(valueFromEntityField);
    }
}
