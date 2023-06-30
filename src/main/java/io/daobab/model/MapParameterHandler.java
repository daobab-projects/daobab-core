package io.daobab.model;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface MapParameterHandler<E extends Entity> {

    <X> X getColumnParam(String key);

    <X> E setColumnParam(String key, X param);
}
