package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public class EntityMandatory extends DaobabException {

    private static final long serialVersionUID = 1L;

    public EntityMandatory() {
        super("Entity is mandatory");
    }

    public EntityMandatory(String msg) {
        super(msg);
    }

    public EntityMandatory(String msg, Throwable th) {
        super(msg, th);
    }

}
