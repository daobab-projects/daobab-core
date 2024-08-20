package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class AttemptToSetWhereClauseSecondTimeException extends DaobabException {

    public AttemptToSetWhereClauseSecondTimeException() {
        super("Where clause should be set once.");
    }


}
