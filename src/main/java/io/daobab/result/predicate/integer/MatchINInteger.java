package io.daobab.result.predicate.integer;

import io.daobab.result.predicate.WherePredicate;

import java.util.Collection;
import java.util.Collections;

public class MatchINInteger implements WherePredicate<Integer> {

    protected final Collection<Integer> valueToCompare;

    @SuppressWarnings("unchecked")
    public MatchINInteger(Object value) {
        if (value instanceof Collection) {
            this.valueToCompare = (Collection<Integer>) value;
        } else {
            this.valueToCompare = Collections.singletonList((Integer) value);
        }
    }

    public boolean test(Integer valueFromEntityField) {
        if (valueFromEntityField == null) {
            return false;
        }
        for (Integer oneOf : valueToCompare) {
            if (oneOf == valueFromEntityField.intValue()) return true;
        }
        return false;
    }
}
