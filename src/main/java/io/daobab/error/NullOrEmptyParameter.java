package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class NullOrEmptyParameter extends DaobabException {

    private static final long serialVersionUID = 1L;

    public NullOrEmptyParameter(String param) {
        super("Parameter " + param + "can not be null or empty");
    }

}
