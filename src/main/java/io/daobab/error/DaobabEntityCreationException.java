package io.daobab.error;

import static java.lang.String.format;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class DaobabEntityCreationException extends RuntimeException {

    private static final long serialVersionUID = -1127875855361548L;

    public DaobabEntityCreationException(Class<?> clazz, Exception e) {
        super(format("Daobab cannot create an instance of object %s. Necessary constructors (no-args and Map<String,Object>) may be not available.  ", clazz.getName()), e);
    }

}
