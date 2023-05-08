package io.daobab.result.predicate;


/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class MatchNotNull extends MatchEQ {

    public MatchNotNull() {
        super(null);
    }

    @Override
    public boolean test(Object valueFromEntityField) {
        return valueFromEntityField != null;
    }
}
