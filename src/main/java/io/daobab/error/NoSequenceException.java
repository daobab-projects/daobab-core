package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public class NoSequenceException extends DaobabException {

    private static final long serialVersionUID = 1L;

    public NoSequenceException() {
        super("Entity has no sequence.");
    }

}
