package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class NoSequenceException extends DaobabException {

    public NoSequenceException() {
        super("Entity has no sequence.");
    }

}
