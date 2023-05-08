package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class NullConsumer extends DaobabException {

    private static final long serialVersionUID = 1L;

    public NullConsumer() {
        super("Null Consumer");
    }

}
