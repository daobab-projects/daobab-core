package io.daobab.result.predicate;


public class AlwaysFalse implements WherePredicate<Object> {

    public boolean test(Object valueFromEntityField) {
        return false;
    }

}
