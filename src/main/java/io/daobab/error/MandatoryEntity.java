package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public class MandatoryEntity extends DaobabException {

    private static final long serialVersionUID = 1L;

    public MandatoryEntity() {
        super("Entity is mandatory");
    }

    public MandatoryEntity(String msg) {
        super(msg);
    }

    public MandatoryEntity(String msg, Throwable th) {
        super(msg, th);
    }

}
