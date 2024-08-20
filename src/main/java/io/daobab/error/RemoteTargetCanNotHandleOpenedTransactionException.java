package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class RemoteTargetCanNotHandleOpenedTransactionException extends DaobabException {

    public RemoteTargetCanNotHandleOpenedTransactionException() {
        super("Attempt to execute query with manual transaction management. This feature is unavailable for remote targets.");
    }

}
