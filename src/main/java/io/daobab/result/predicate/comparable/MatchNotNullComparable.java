package io.daobab.result.predicate.comparable;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class MatchNotNullComparable<V extends Comparable<V>> extends MatchEQComparable<V> {

    public MatchNotNullComparable() {
        super(null);
    }

    @Override
    public boolean test(V valueFromEntityField) {
        return valueFromEntityField != null;
    }
}
