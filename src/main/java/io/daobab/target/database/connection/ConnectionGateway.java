package io.daobab.target.database.connection;

import io.daobab.error.DaobabSQLException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public interface ConnectionGateway {

    static void closeConnectionIfOpened(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) conn.close();
            } catch (SQLException e) {
                throw new DaobabSQLException(e);
            }
        }
    }

}
