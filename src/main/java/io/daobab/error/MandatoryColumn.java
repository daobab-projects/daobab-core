package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class MandatoryColumn extends DaobabException {

    public MandatoryColumn() {
        super("Column is mandatory");
    }

    public MandatoryColumn(String msg) {
        super(msg);
    }

    public MandatoryColumn(String msg, Throwable th) {
        super(msg, th);
    }

}
