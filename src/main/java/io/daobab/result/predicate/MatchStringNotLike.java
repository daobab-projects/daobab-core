package io.daobab.result.predicate;


public class MatchStringNotLike extends MatchStringLike {

    public MatchStringNotLike(String valueToCompare) {
        super(valueToCompare);
    }

    public boolean test(String valueFromEntityField) {
        return !super.test(valueFromEntityField);
    }

}
