package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public class QueryMandatory extends DaobabException {

    private static final long serialVersionUID = 1L;

    public QueryMandatory() {
        super("Query is mandatory.");
    }

}
