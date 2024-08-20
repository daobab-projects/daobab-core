package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class ReadRemoteException extends DaobabException {

    public ReadRemoteException(Exception e) {
        super("Problem with reading remote data.", e);
    }

}
