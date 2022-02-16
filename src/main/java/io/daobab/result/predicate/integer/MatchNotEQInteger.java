package io.daobab.result.predicate.integer;

import io.daobab.result.predicate.WherePredicate;

public class MatchNotEQInteger implements WherePredicate<Integer> {

    protected final int valueToCompare;

    public MatchNotEQInteger(Integer valueToCompare) {
        this.valueToCompare = valueToCompare;
    }

    public boolean test(Integer valueFromEntityField) {
        if (valueFromEntityField == null) {
            return false;
        }
        return valueToCompare != valueFromEntityField;
    }
}
