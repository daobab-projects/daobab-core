package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class InnerQueryMandatory extends DaobabException {

    private static final long serialVersionUID = 1L;


    public InnerQueryMandatory() {
        super("Inner Query is mandatory.");
    }

}
