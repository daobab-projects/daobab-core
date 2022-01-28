package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public class ReadRemoteException extends DaobabException {

    private static final long serialVersionUID = 1L;

    public ReadRemoteException(Exception e) {
        super("Problem with reading remote data.", e);
    }

}
