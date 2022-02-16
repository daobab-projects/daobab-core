package io.daobab.result.predicate.integer;

import io.daobab.result.predicate.WherePredicate;

public class MatchLTInteger implements WherePredicate<Integer> {

    protected final int valueToCompare;

    public MatchLTInteger(Integer valueToCompare) {
        this.valueToCompare = valueToCompare;
    }

    public boolean test(Integer valueFromEntityField) {
        if (valueFromEntityField == null) {
            return false;
        }
        return valueToCompare < valueFromEntityField;
    }
}
