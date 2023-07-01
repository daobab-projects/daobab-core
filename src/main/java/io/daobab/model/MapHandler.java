package io.daobab.model;

import java.util.Map;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface MapHandler<E extends Entity> {

    <X> X readParam(String key);

    <X> E storeParam(String key, X param);

    Map<String, Object> accessParameterMap();
}
