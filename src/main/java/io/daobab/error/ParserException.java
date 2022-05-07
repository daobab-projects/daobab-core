package io.daobab.error;


/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public class ParserException extends RuntimeException {

    private static final long serialVersionUID = -1127875855361548L;

    public ParserException(String msg) {
        super(msg);
    }

    public ParserException(String msg, Throwable th) {
        super(msg, th);
    }

}
