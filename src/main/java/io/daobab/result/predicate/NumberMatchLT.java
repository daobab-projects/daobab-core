package io.daobab.result.predicate;


/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class NumberMatchLT extends NumberMatchEQ {

    public NumberMatchLT(Object valueToCompare) {
        super(valueToCompare);
    }

    @Override
    public boolean test(Object valueFromEntityField) {
        return valueFromEntityField != null && valueToCompare > (((Number) valueFromEntityField).doubleValue());
    }
}
