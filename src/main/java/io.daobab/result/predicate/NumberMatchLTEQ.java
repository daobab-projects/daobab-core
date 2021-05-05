package io.daobab.result.predicate;


public class NumberMatchLTEQ extends NumberMatchEQ {


    public NumberMatchLTEQ(Object valueToCompare) {
        super(valueToCompare);
    }

    public boolean test(Object valueFromEntityField) {
        return valueFromEntityField != null && valueToCompare >= (((Number) valueFromEntityField).doubleValue());
    }
}
