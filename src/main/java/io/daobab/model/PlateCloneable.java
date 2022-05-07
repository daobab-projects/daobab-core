package io.daobab.model;


import java.util.HashMap;
import java.util.Map;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public interface PlateCloneable extends Map<String, Map<String, Object>>, Cloneable {


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
