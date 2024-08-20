package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class NullParameter extends DaobabException {

    public NullParameter(String param) {
        super("Parameter " + param + "can not be null");
    }

}
