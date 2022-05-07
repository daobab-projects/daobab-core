package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class RemoteTargetCanNotHandleOpenedTransactionException extends DaobabException {

    private static final long serialVersionUID = 1L;

    public RemoteTargetCanNotHandleOpenedTransactionException() {
        super("You are trying to execute query with manual transaction management. This feature is unavailable to remote targets.");
    }

}
