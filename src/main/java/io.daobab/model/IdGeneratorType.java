package io.daobab.model;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public enum IdGeneratorType {

    // DB sequence - needs getSequence
    SEQUENCE,
    //ID generated by database
    AUTO_INCREMENT,
    // no action
    NONE,
    //inner generator
    GENERATOR
}
