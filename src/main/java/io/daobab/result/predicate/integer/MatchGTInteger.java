package io.daobab.result.predicate.integer;

import io.daobab.result.predicate.WherePredicate;

public class MatchGTInteger implements WherePredicate<Integer> {

    protected final int valueToCompare;

    public MatchGTInteger(Integer valueToCompare) {
        this.valueToCompare = valueToCompare;
    }

    public boolean test(Integer valueFromEntityField) {
        if (valueFromEntityField == null) {
            return false;
        }
        return valueToCompare > valueFromEntityField;
    }
}
