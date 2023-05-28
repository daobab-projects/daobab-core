package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class RemoteTargetCanNotHandleOpenedTransactionException extends DaobabException {

    private static final long serialVersionUID = 1L;

    public RemoteTargetCanNotHandleOpenedTransactionException() {
        super("Attempt to execute query with manual transaction management. This feature is unavailable for remote targets.");
    }

}
