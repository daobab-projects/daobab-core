package io.daobab.result.predicate.time;

import io.daobab.result.predicate.WherePredicate;

import java.time.temporal.Temporal;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class MatchLTTemporal<V extends Temporal & Comparable<? super V>> implements WherePredicate<V> {

    protected final V valueToCompare;

    public MatchLTTemporal(V valueToCompare) {
        this.valueToCompare = valueToCompare;
    }

    public boolean test(V valueFromEntityField) {
        return valueToCompare.compareTo(valueFromEntityField) > 0;
    }
}
