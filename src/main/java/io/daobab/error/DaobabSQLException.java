package io.daobab.error;

import java.sql.SQLException;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class DaobabSQLException extends DaobabException {

    private static final long serialVersionUID = -1127875855361548L;

    private SQLException nextException;

    public DaobabSQLException(SQLException sqlexception) {
        super(sqlexception.getMessage());
        setNextException(sqlexception);
        initCause(sqlexception);
    }

    public DaobabSQLException(String message, SQLException sqlexception) {
        super(message);
        setNextException(sqlexception);
        initCause(sqlexception);
    }

    public SQLException getNextException() {
        return nextException;
    }

    public void setNextException(SQLException nextException) {
        this.nextException = nextException;
    }
}
