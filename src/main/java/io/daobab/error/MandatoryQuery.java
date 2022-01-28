package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public class MandatoryQuery extends DaobabException {

    private static final long serialVersionUID = 1L;

    public MandatoryQuery() {
        super("Query is mandatory.");
    }

}
