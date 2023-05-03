package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class Unfinished extends DaobabException {

    private static final long serialVersionUID = 1L;

    public Unfinished() {
        super("Unfinished functionality");
    }

    public Unfinished(String msg) {
        super(msg);
    }

}
