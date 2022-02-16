package io.daobab.result.predicate.integer;

import io.daobab.result.predicate.WherePredicate;

public class MatchEQInteger implements WherePredicate<Integer> {

    protected final int valueToCompare;

    public MatchEQInteger(Integer valueToCompare) {
        this.valueToCompare = valueToCompare;
    }

    public boolean test(Integer valueFromEntityField) {
        if (valueFromEntityField == null) {
            return false;
        }
        return valueToCompare == valueFromEntityField;
    }

}
