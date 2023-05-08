package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class SqlInjectionDetected extends DaobabException {

    private static final long serialVersionUID = 1L;

    public SqlInjectionDetected(String content) {
        super("SQL Injection attempt has been detected: " + content);
    }

}
