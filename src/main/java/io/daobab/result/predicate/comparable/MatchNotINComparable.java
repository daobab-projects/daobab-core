package io.daobab.result.predicate.comparable;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */

public class MatchNotINComparable<V extends Comparable<V>> extends MatchINComparable<V> {

    @SuppressWarnings("unchecked")
    public MatchNotINComparable(Object value) {
        super(value);
    }

    public boolean test(V valueFromEntityField) {
        for (V oneOf : valueToCompare) {
            if (oneOf.compareTo(valueFromEntityField) != 0) return true;
        }
        return false;
    }
}
