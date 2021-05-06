package io.daobab.result.predicate;

public class NumberMatchEQ implements WherePredicate<Object> {

    protected final double valueToCompare;

    public NumberMatchEQ(Object valueToCompare) {
        this.valueToCompare = ((Number) valueToCompare).doubleValue();
    }


    public boolean test(Object valueFromEntityField) {
        return valueFromEntityField != null && valueToCompare == (((Number) valueFromEntityField).doubleValue());
    }


}
