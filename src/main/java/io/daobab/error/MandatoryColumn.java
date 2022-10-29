package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public class MandatoryColumn extends DaobabException {

    private static final long serialVersionUID = 1L;

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
