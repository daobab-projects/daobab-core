package io.daobab.result.predicate;


public class NumberMatchLT extends NumberMatchEQ {

    public NumberMatchLT(Object valueToCompare) {
        super(valueToCompare);
    }

    @Override
    public boolean test(Object valueFromEntityField) {
        return valueFromEntityField != null && valueToCompare > (((Number) valueFromEntityField).doubleValue());
    }
}
