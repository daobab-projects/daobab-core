package io.daobab.result.predicate;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class MatchNotEQ extends MatchEQ {

    public MatchNotEQ(Object valueToCompare) {
        super(valueToCompare);
    }

    @Override
    public boolean test(Object valueFromEntityField) {
        return !super.test(valueFromEntityField);
    }
}
