package io.daobab.model;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public interface EntityRelation<E extends Entity> extends Entity {

    default E getEntity() {
        return (E) this;
    }


}
