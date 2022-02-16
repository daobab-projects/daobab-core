package io.daobab.result.predicate.comparable;

import io.daobab.result.predicate.WherePredicate;

public class MatchLTComparable<V extends Comparable<V>> implements WherePredicate<V> {

    protected final V valueToCompare;

    public MatchLTComparable(V valueToCompare) {
        this.valueToCompare = valueToCompare;
    }

    public boolean test(V valueFromEntityField) {
        return valueToCompare.compareTo(valueFromEntityField) > 0;
    }
}
