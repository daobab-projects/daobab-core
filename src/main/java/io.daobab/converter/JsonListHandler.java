package io.daobab.converter;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public interface JsonListHandler extends JsonHandler {


    default String toJSON() {
        return toJSON(this);
    }

}
