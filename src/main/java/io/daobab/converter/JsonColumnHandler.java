package io.daobab.converter;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public interface JsonColumnHandler extends JsonHandler {


    default String toJSON() {

        StringBuilder rv = new StringBuilder();
        rv.append("{");

        rv.append("}");


        return rv.toString();
    }

}
