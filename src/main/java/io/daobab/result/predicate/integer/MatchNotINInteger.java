package io.daobab.result.predicate.integer;


public class MatchNotINInteger extends MatchINInteger {

    public MatchNotINInteger(Object value) {
        super(value);
    }

    public boolean test(Integer valueFromEntityField) {
        for (Integer oneOf : valueToCompare) {
            if (oneOf != valueFromEntityField.intValue()) return true;
        }
        return false;
    }
}
