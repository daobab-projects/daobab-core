package io.daobab.result.predicate.comparable;


public class MatchIsNullComparable<V extends Comparable<V>> extends MatchEQComparable<V> {

    public MatchIsNullComparable() {
        super(null);
    }

    @Override
    public boolean test(V valueFromEntityField) {
        return valueFromEntityField == null;
    }
}
