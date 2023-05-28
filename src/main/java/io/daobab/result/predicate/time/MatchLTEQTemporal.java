package io.daobab.result.predicate.time;

import io.daobab.result.predicate.WherePredicate;

import java.time.temporal.Temporal;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class MatchLTEQTemporal<V extends Temporal & Comparable<? super V>> implements WherePredicate<V> {

    protected final V valueToCompare;

    public MatchLTEQTemporal(V valueToCompare) {
        this.valueToCompare = valueToCompare;
    }

    public boolean test(V valueFromEntityField) {
        return valueToCompare.compareTo(valueFromEntityField) >= 0;
    }
}
