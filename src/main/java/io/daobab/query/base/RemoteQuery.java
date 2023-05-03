package io.daobab.query.base;

import java.util.Map;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface RemoteQuery<Q extends Query> {

    Map<String, Object> toRemote(boolean singleResult);

}
