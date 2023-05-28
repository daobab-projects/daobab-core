package io.daobab.model;

import java.util.Map;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings("unused")
public interface FlatPlate extends Map<String, Object>, Entity {

    void fromPlate(Plate plate);


}
