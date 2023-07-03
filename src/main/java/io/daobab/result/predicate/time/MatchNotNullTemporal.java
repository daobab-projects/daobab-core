package io.daobab.result.predicate.time;

import java.time.temporal.Temporal;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class MatchNotNullTemporal<V extends Temporal & Comparable<? super V>> extends MatchEQTemporal<V> {

    public MatchNotNullTemporal() {
        super(null);
    }

    @Override
    public boolean test(V valueFromEntityField) {
        return valueFromEntityField != null;
    }
}
