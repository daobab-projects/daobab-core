package io.daobab.target;

import io.daobab.internallogger.ILoggerBean;
import io.daobab.model.Entity;
import io.daobab.target.protection.AccessProtectorProvider;

import java.util.List;
import java.util.function.Supplier;

/**
 * The backend a Daobab query runs against - a database, an in-memory buffer, a remote endpoint, ... It knows the
 * entities it manages ({@link #getTables()}), resolves their names ({@link #getEntityName(Class)}), reports
 * whether a transaction is active, and can wrap a unit of work in a transaction ({@link #aroundTransaction}).
 * Being an {@link AccessProtectorProvider} it also carries the column/entity access protection.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface Target extends ILoggerBean, AccessProtectorProvider {

    /**
     * The entities this target manages.
     */
    List<Entity> getTables();

    /** Runs the supplier, giving the target a chance to wrap it in a transaction (runs it as-is by default). */
    default <T> T aroundTransaction(Supplier<T> t) {
        return t.get();
    }

    /** Whether a transaction is currently active on this target. */
    boolean isTransactionActive();

    /** The (table) name of the given entity class on this target. */
    String getEntityName(Class<? extends Entity> entityClass);


}
