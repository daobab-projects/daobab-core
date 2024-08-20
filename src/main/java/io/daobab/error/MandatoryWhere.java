package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class MandatoryWhere extends DaobabException {

    public MandatoryWhere() {
        super("Where clause is mandatory for this operation.");
    }

}
