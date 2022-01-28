package io.daobab.result.predicate.integer;


public class MatchNotNullInteger extends MatchEQInteger {

    public MatchNotNullInteger() {
        super(0);
    }

    @Override
    public boolean test(Integer valueFromEntityField) {
        return valueFromEntityField != null;
    }
}
