package io.daobab.result.predicate;

public class AlwaysTrue implements WherePredicate<Object> {

    public boolean test(Object valueFromEntityField) {
        return true;
    }
}
