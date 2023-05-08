package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class NullEntityException extends DaobabException {

    private static final long serialVersionUID = 1L;

    public NullEntityException() {
        super("Null Entity");
    }

}
