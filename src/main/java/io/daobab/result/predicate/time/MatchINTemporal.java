package io.daobab.result.predicate.time;

import io.daobab.result.predicate.WherePredicate;

import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class MatchINTemporal<V extends Temporal & Comparable<? super V>> implements WherePredicate<V> {

    protected final Collection<V> valueToCompare;

    @SuppressWarnings("unchecked")
    public MatchINTemporal(Object value) {
        if (value instanceof Collection) {
            this.valueToCompare = (Collection<V>) value;
        } else {
            this.valueToCompare = Collections.singletonList((V) value);
        }
    }

    public boolean test(V valueFromEntityField) {
        for (V oneOf : valueToCompare) {
            if (oneOf.compareTo(valueFromEntityField) == 0) return true;
        }
        return false;
    }
}
