package io.daobab.result.predicate;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class MatchEQ implements WherePredicate<Object> {

    protected final Object valueToCompare;

    public MatchEQ(Object valueToCompare) {
        this.valueToCompare = valueToCompare;
    }

    public boolean test(Object valueFromEntityField) {
        return valueToCompare.equals(valueFromEntityField);
    }
}
