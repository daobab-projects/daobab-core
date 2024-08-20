package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class MandatoryInnerQuery extends DaobabException {

    public MandatoryInnerQuery() {
        super("Inner Query is mandatory.");
    }

}
