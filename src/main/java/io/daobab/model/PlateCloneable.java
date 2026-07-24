package io.daobab.model;


import java.util.HashMap;
import java.util.Map;

/**
 * A {@link Plate} (a map from entity name to that entity's column values) that can be cloned - the base of the
 * multi-entity row representation produced by "plate" queries.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface PlateCloneable extends Map<String, Map<String, Object>>, Cloneable {


    /**
     * A new {@link Plate} cloned from this one.
     */
    default Plate clone() {

        Plate clone = new Plate();

        for (String entity : this.keySet()) {
            Map<String, Object> entitymap = this.get(entity);
            Map<String, Object> cloneentitymap = new HashMap<>();


            for (String col : entitymap.keySet()) {


            }

        }

        return clone;
    }

}
