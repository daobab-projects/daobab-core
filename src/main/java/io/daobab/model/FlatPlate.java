package io.daobab.model;

import java.util.Map;

/**
 * A flattened plate: a single {@code fieldName -> value} map (rather than the per-entity nesting of a
 * {@link Plate}), also usable as an {@link Entity}. Filled from a plate through {@link #fromPlate(Plate)}.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings("unused")
public interface FlatPlate extends Map<String, Object>, Entity {

    /**
     * Fills this flat plate from the given plate.
     */
    void fromPlate(Plate plate);


}
