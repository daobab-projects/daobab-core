package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class DaobabEntityCreationException extends RuntimeException {

    private static final long serialVersionUID = -1127875855361548L;

    public DaobabEntityCreationException(Class<?> clazz, Exception e) {
        super("No-args contructor is not available thus Daobab cannot create an instance of object " + clazz.getName(), e);
    }


}
