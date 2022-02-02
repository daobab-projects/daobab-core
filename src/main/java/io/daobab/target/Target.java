package io.daobab.target;

import io.daobab.internallogger.ILoggerBean;
import io.daobab.model.Entity;
import io.daobab.target.protection.AccessProtectorProvider;

import java.util.List;
import java.util.function.Supplier;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public interface Target extends ILoggerBean, AccessProtectorProvider {

    boolean isBuffer();

    boolean isConnectedToDatabase();


    List<Entity> getTables();

    default <T> T aroundTransaction(Supplier<T> t) {
        return t.get();
    }

    boolean isTransactionActive();

    boolean isLogQueriesEnabled();


}
