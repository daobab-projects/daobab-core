package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class MandatoryQuery extends DaobabException {

    public MandatoryQuery() {
        super("Query is mandatory.");
    }

}
