package io.daobab.result.predicate;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class AlwaysFalse implements WherePredicate<Object> {

    public boolean test(Object valueFromEntityField) {
        return false;
    }

}
