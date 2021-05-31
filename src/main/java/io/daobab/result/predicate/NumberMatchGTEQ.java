package io.daobab.result.predicate;


public class NumberMatchGTEQ extends NumberMatchEQ {

    public NumberMatchGTEQ(Object valueToCompare) {
        super(valueToCompare);
    }

    @Override
    public boolean test(Object valueFromEntityField) {
        return valueFromEntityField != null && valueToCompare <= (((Number) valueFromEntityField).doubleValue());
    }
}
