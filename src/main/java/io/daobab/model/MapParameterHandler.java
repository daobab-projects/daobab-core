package io.daobab.model;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface MapParameterHandler {

    <X> X getColumnParam(String key);

    <X> void setColumnParam(String key, X param);
}
