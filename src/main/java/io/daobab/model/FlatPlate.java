package io.daobab.model;

import io.daobab.error.DaobabException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings("unused")
public interface FlatPlate extends Map<String, Object>, EntityMap {

    void fromPlate(Plate plate);


}
