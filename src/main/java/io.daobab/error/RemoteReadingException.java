package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class RemoteReadingException extends DaobabException {

    private static final long serialVersionUID = 1L;


    public RemoteReadingException(Exception e) {
        super("Problem with reading remote data.", e);
    }


}
