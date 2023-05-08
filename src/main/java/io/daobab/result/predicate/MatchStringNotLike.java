package io.daobab.result.predicate;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class MatchStringNotLike extends MatchStringLike {

    public MatchStringNotLike(String valueToCompare) {
        super(valueToCompare);
    }

    public boolean test(String valueFromEntityField) {
        return !super.test(valueFromEntityField);
    }

}
