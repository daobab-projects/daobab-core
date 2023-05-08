package io.daobab.result.predicate;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class MatchIN implements WherePredicate<Object> {

    private final Collection<Object> valueToCompare;

    public MatchIN(Object valueToCompare) {
        if (valueToCompare instanceof Collection) {
            this.valueToCompare = (Collection<Object>) valueToCompare;
        } else {
            this.valueToCompare = Collections.singletonList(valueToCompare);
        }
    }


    public boolean test(Object valueFromEntityField) {
        for (Object enttfieldval : valueToCompare) {
            if (valueFromEntityField.equals(enttfieldval)) return true;
        }
        return false;
    }
}
