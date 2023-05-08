package io.daobab.result.predicate.comparable;

import io.daobab.result.predicate.WherePredicate;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class MatchFalseComparable<V extends Comparable<V>> implements WherePredicate<V> {

    protected final V valueToCompare;

    public MatchFalseComparable(V valueToCompare) {
        this.valueToCompare = valueToCompare;
    }

    public boolean test(V valueFromEntityField) {
        return false;
    }
}
