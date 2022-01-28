package io.daobab.result.predicate.integer;

import io.daobab.result.predicate.WherePredicate;

public class MatchFalseInteger<V extends Comparable<V>> implements WherePredicate<V> {

    protected final V valueToCompare;

    public MatchFalseInteger(V valueToCompare) {
        this.valueToCompare = valueToCompare;
    }

    public boolean test(V valueFromEntityField) {
        return false;
    }
}
