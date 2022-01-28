package io.daobab.result.predicate.integer;

import io.daobab.result.predicate.WherePredicate;

public class MatchLTEQInteger implements WherePredicate<Integer> {

    protected final int valueToCompare;

    public MatchLTEQInteger(Integer valueToCompare) {
        this.valueToCompare = valueToCompare;
    }

    public boolean test(Integer valueFromEntityField) {
        if (valueFromEntityField == null) {
            return false;
        }
        return valueToCompare <= valueFromEntityField;
    }
}
