package io.daobab.model;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public interface MapParameterHandler {

    <X> X getColumnParam(String key);

    <X> void setColumnParam(String key, X param);
}
