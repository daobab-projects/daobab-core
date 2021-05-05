package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class NullParameter extends DaobabException {

    private static final long serialVersionUID = 1L;


    public NullParameter(String param) {
        super("Parameter " + param + "can not be null");
    }


}
