package io.daobab.result.predicate;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class AlwaysTrue implements WherePredicate<Object> {

    public boolean test(Object valueFromEntityField) {
        return true;
    }
}
