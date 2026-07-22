package io.daobab.query.base;

import java.util.Map;

/**
 * A query that can serialize itself to a transport map, so it may be executed on a remote target.
 *
 * @param <Q> the concrete query type
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface RemoteQuery<Q extends Query> {

    /**
     * Serializes the query to a transport map.
     *
     * @param singleResult whether the caller expects a single result rather than many
     * @return the transport representation
     */
    Map<String, Object> toRemote(boolean singleResult);

}
