package io.daobab.result.predicate.time;

import java.time.temporal.Temporal;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */

public class MatchNotINTemporal<V extends Temporal & Comparable<? super V>> extends MatchINTemporal<V> {

    @SuppressWarnings("unchecked")
    public MatchNotINTemporal(Object value) {
        super(value);
    }

    public boolean test(V valueFromEntityField) {
        for (V oneOf : valueToCompare) {
            if (oneOf.compareTo(valueFromEntityField) != 0) return true;
        }
        return false;
    }
}
