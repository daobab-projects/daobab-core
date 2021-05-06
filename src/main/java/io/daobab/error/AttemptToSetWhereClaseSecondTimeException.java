package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class AttemptToSetWhereClaseSecondTimeException extends DaobabException {

    private static final long serialVersionUID = 1L;


    public AttemptToSetWhereClaseSecondTimeException() {
        super("Where clause should be set once.");
    }


}
