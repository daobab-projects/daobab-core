package io.daobab.error;

import io.daobab.target.Target;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public class NoDataBaseConnectionException extends DaobabException {

    private static final long serialVersionUID = 1L;

    public NoDataBaseConnectionException(Target dao) {
        super("Target " + dao.getClass().getName() + " has no database connection.");
    }

    public NoDataBaseConnectionException(String msg) {
        super(msg);
    }


    public NoDataBaseConnectionException(String msg, Throwable th) {
        super(msg, th);
    }

}
