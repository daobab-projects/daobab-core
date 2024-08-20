package io.daobab.error;

import java.sql.SQLException;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class DaobabSQLException extends DaobabException {

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
