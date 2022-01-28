package io.daobab.result.predicate.integer;


public class MatchIsNullInteger extends MatchEQInteger {

    public MatchIsNullInteger() {
        super(0);
    }

    @Override
    public boolean test(Integer valueFromEntityField) {
        return valueFromEntityField == null;
    }
}
