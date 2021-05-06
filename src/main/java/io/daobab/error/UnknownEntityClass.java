package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
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
