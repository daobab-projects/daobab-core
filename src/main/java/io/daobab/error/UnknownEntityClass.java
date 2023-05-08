package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class UnknownEntityClass extends DaobabException {

    private static final long serialVersionUID = 1L;

    public UnknownEntityClass() {
        super("Unknown Entity Class");
    }

    public UnknownEntityClass(String msg) {
        super(msg);
    }

}
