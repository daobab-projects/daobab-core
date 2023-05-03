package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class PrimaryKeyHasNoValue extends DaobabException {

    private static final long serialVersionUID = 1L;

    public PrimaryKeyHasNoValue() {
        super("Primary Key has no value. Is this entity saved?");
    }

}
