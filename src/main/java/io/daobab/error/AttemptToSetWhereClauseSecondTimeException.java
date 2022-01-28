package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public class AttemptToSetWhereClauseSecondTimeException extends DaobabException {

    private static final long serialVersionUID = 1L;

    public AttemptToSetWhereClauseSecondTimeException() {
        super("Where clause should be set once.");
    }


}
