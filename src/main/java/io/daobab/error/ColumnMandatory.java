package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class ColumnMandatory extends DaobabException {

    private static final long serialVersionUID = 1L;

    public ColumnMandatory() {
        super("Column is mandatory");
    }

    public ColumnMandatory(String msg) {
        super(msg);
    }

    public ColumnMandatory(String msg, Throwable th) {
        super(msg, th);
    }

}
