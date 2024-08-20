package io.daobab.error;


/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class ParserException extends RuntimeException {

    public ParserException(String msg) {
        super(msg);
    }

    public ParserException(String msg, Throwable th) {
        super(msg, th);
    }

}
